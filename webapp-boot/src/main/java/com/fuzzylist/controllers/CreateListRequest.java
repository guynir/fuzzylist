package com.fuzzylist.controllers;

/**
 * Web request to create a new list.
 *
 * @param title       List title.
 * @param leftToRight {@code true} for a left-to-right display, {@code false} right-to-left display.
 * @author Guy Raz Nir
 * @since 2022/03/20
 */
public record CreateListRequest(String title, Boolean leftToRight) {
}
