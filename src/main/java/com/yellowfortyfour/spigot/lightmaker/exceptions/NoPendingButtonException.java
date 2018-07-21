package com.yellowfortyfour.spigot.lightmaker.exceptions;

public class NoPendingButtonException extends Exception {
  static final long serialVersionUID = 1L;

  public NoPendingButtonException() {
  }

  public NoPendingButtonException(String message) {
    super(message);
  }
}