package com.fuzzylist.controllers;

/**
 * Web request to add a text to a list.
 *
 * @param text Text to add.
 *
 * @author Guy Raz Nir
 * @since 2022/03/20
 */
public record AddTextRequest(String text) {
}
