package edu.calpoly.csc3100.fantasy;

/**
 * undefined
 * @author CSC 3100 Instructional Team
 * @version 1.0
 */
public final class Sword implements Character {
  private final Character wrapped;
  public Sword(Character wrapped) { this.wrapped = java.util.Objects.requireNonNull(wrapped); }
  @Override public void draw(java.awt.Graphics2D g, int x, int y) {
    wrapped.draw(g, x, y);
    // TODO: draw a visible sword to the right of the body, within the tile.
    g.setColor(java.awt.Color.LIGHT_GRAY);
    g.fillRect(x + 33, y + 8, 3, 18);
    g.setColor(java.awt.Color.DARK_GRAY);
    g.fillRect(x + 29, y + 19, 11, 3);
    g.fillRect(x + 33, y + 25, 3, 5);
  }

  public int attack() { return wrapped.attack() + 5; /* TODO: add 5 */ }
  public int defense() { return wrapped.defense(); }
  public String type() { return wrapped.type(); }
}
