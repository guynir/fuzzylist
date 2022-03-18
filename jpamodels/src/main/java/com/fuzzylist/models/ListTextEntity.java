package com.fuzzylist.models;

import com.fuzzylist.common.ObjectBagAware;
import com.fuzzylist.common.ObjectBag;
import jakarta.persistence.*;

@Entity
@Table(name = "list_text", uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "index"}))
public class ListTextEntity extends ObjectBagAware {

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
    public ListEntity parent;

    /**
     * Index of text within the table.
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
    public ListTextEntity() {
    }

    /**
     * Class constructor.
     *
     * @param parent Parent list.
     * @param index  Index of text within the list.
     * @param text   Text.
     */
    public ListTextEntity(ListEntity parent, Integer index, String text) {
        this.parent = parent;
        this.index = index;
        this.text = text;
    }

    @Override
    protected ObjectBag state() {
        return new ObjectBag(id, parent, index, text);
    }
}
