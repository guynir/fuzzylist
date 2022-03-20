package com.fuzzylist.controllers;

import com.fuzzylist.models.ListEntity;
import com.fuzzylist.models.ListTextEntity;
import com.fuzzylist.services.ListManagementService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fuzzylist.controllers.ControllerConstants.API_V1_PREFIX;

/**
 * Spring REST controller for accessing lists and list text entries.
 *
 * @author Guy Raz Nir
 * @since 2022/03/17
 */
@RestController
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
    @GetMapping(value = API_V1_PREFIX + "/list/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListRecord> fetchLists() {
        return service.fetchLists()
                .stream().map(e -> new ListRecord(e.key, e.title, e.leftToRight))
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
    @GetMapping(value = API_V1_PREFIX + "/list/{listKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListPageRecord fetchListText(@PathVariable("listKey") String listKey,
                                        @RequestParam(name = "startAfter", required = false) Integer startIndex,
                                        @RequestParam(name = "limit", required = false) Integer limit,
                                        @RequestParam(name = "order", required = false, defaultValue = "ASC") String order) {
        // Fetch list entries.
        boolean ascending = parseOrder(order);
        List<ListTextEntity> entries = service.fetchListTexts(listKey, startIndex, limit, ascending);

        // Translate response to web records.
        List<ListTextRecord> records = entries.stream().map(WebResponseFactory::toRecord).toList();

        return new ListPageRecord(records);
    }

    /**
     * Creates a new list.
     *
     * @param request Request containing list information.
     * @return List response containing only list key.
     */
    @PostMapping(value = API_V1_PREFIX + "/list/")
    public ListRecord createList(@RequestBody CreateListRequest request) {
        ListEntity listEntity = service.createList(request.title(),
                request.leftToRight() != null ? request.leftToRight() : true);
        return new ListRecord(listEntity.key, null, null);
    }

    /**
     * Add a new text to a list.
     *
     * @param listKey List key.
     * @param request Request containing the text.
     */
    @PostMapping(value = API_V1_PREFIX + "/list/{listKey}/")
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
