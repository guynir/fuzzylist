package com.fuzzylist.controllers;

/**
 * Represents an HTTP response describing a list.
 *
 * @param key         List key - represents the public identifier of the list.
 * @param title       List title.
 * @param leftToRight {@code true} - if list should be displayed from left-to-right, {@code false} if right-to-left.
 */
public record ListRecord(String key, String title, boolean leftToRight) {
}
