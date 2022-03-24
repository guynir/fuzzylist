package com.fuzzylist.models;


import com.fuzzylist.common.ObjectBag;
import com.fuzzylist.common.ObjectBagAware;
import jakarta.persistence.*;

@Entity
@Table(name = "list_header")
public class ListHeaderEntity extends ObjectBagAware {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * Unique key that publicly identifies the list.
     */
    @Column(nullable = false, length = 200)
    public String key;

    /**
     * List name/title.
     */
    @Column(nullable = false, length = 500)
    public String title;

    /**
     * Indicates if the list text flow is left-to-right ({@code true}) or right-to-left ({@code false}).
     */
    @Column(nullable = false)
    public boolean leftToRight = true;

    /**
     * Class constructor.
     */
    public ListHeaderEntity() {
    }

    /**
     * Class constructor.
     *
     * @param key         List key.
     * @param title       Title.
     * @param leftToRight Indicator of LTR or RTL.
     */
    public ListHeaderEntity(String key, String title, boolean leftToRight) {
        this.key = key;
        this.title = title;
        this.leftToRight = leftToRight;
    }

    @Override
    protected ObjectBag state() {
        return new ObjectBag(id, key, title, leftToRight);
    }
}
