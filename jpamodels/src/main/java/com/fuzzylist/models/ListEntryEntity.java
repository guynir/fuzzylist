package com.fuzzylist.models;

import com.fuzzylist.common.ObjectBagAware;
import com.fuzzylist.common.ObjectBag;
import jakarta.persistence.*;

@Entity
@Table(name = "list_entry", uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "index"}))
public class ListEntryEntity extends ObjectBagAware {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * Parent list of this text.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public ListHeaderEntity parent;

    /**
     * Index of the text within the table.
     */
    @Column(nullable = false)
    public Integer index;

    /**
     * Text entry.
     */
    @Column(nullable = false, length = 1024)
    public String text;

    /**
     * Class constructor.
     */
    public ListEntryEntity() {
    }

    /**
     * Class constructor.
     *
     * @param parent Parent list.
     * @param index  Index of text within the list.
     * @param text   Text.
     */
    public ListEntryEntity(ListHeaderEntity parent, Integer index, String text) {
        this.parent = parent;
        this.index = index;
        this.text = text;
    }

    @Override
    protected ObjectBag state() {
        return new ObjectBag(id, parent, index, text);
    }
}
