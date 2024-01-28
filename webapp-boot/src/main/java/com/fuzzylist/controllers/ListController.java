package com.fuzzylist.controllers;

import com.fuzzylist.models.ListHeaderEntity;
import com.fuzzylist.services.ListEntries;
import com.fuzzylist.services.ListManagementService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fuzzylist.controllers.ControllerConstants.API_V1_PREFIX;
import static com.fuzzylist.controllers.WebResponseFactory.toRecord;

/**
 * Spring REST controller for accessing lists and list text entries.
 *
 * @author Guy Raz Nir
 * @since 2022/03/17
 */
@RestController
@RequestMapping(API_V1_PREFIX)
public class ListController {

    private final ListManagementService service;

    /**
     * Class constructor.
     */
    public ListController(ListManagementService service) {
        this.service = service;
    }

    /**
     * @return List of all available lists in the server.
     */
    @GetMapping(value = "/list/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HeaderResponse> fetchLists() {
        return service.fetchLists()
                .stream().map(e -> new HeaderResponse(e.key, e.title, e.leftToRight))
                .collect(Collectors.toList());
    }

    /**
     * Fetch text records for a list.
     *
     * @param listKey    List key.
     * @param startIndex Optional starting index (exclusive).
     * @param limit      Optional number of texts to return. If no specified, uses {@link ListManagementService#PAGE_SIZE}.
     * @param order      Optional sorting order (ASC/DESC). If omitted, defaults to 'ASC'. If value is invalid, ASC
     * @return Response containing list of texts.
     */
    @GetMapping(value = "/list/{listKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListDataResponse fetchListText(@PathVariable("listKey") String listKey,
                                          @RequestParam(name = "startAfter", required = false) Integer startIndex,
                                          @RequestParam(name = "limit", required = false) Integer limit,
                                          @RequestParam(name = "order", required = false, defaultValue = "ASC") String order) {
        // Fetch list entries.
        boolean ascending = parseOrder(order);
        ListEntries entries = service.fetchListTexts(listKey, startIndex, limit, ascending);

        return new ListDataResponse(toRecord(entries.listHeaderEntity()), toRecord(entries.entries()));
    }

    /**
     * Creates a new list.
     *
     * @param request Request containing list information.
     * @return List response containing only list key.
     */
    @PostMapping(value =  "/list/")
    public HeaderResponse createList(@RequestBody CreateListRequest request) {
        ListHeaderEntity listHeaderEntity = service.createList(request.title(),
                request.leftToRight() != null ? request.leftToRight() : true);
        return new HeaderResponse(listHeaderEntity.key, null, null);
    }

    /**
     * Add a new text to a list.
     *
     * @param listKey List key.
     * @param request Request containing the text.
     */
    @PostMapping(value =  "/list/{listKey}/")
    public void addListText(@PathVariable("listKey") String listKey, @RequestBody AddTextRequest request) {
        service.addListText(listKey, request.text());
    }

    /**
     * Parse a string indicating the sort order - ASC for ascending, DESC to descending. If order cannot be interpreted,
     * a default value "ASC" is used.
     *
     * @param order String representing order.
     * @return {@code true} if order is ascending, {@code false} for descending.
     */
    private boolean parseOrder(String order) {
        // It's enough to test to "DESC", for any other value, the response should be 'true'.
        return !"DESC".equals(order);
    }
}
