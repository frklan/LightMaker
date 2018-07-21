package com.yellowfortyfour.spigot.lightmaker.exceptions;

public class NoSuchPlayerException extends Exception {
  static final long serialVersionUID = 1L;

  public NoSuchPlayerException() {
  }

  public NoSuchPlayerException(String message) {
    super(message);
  }
}