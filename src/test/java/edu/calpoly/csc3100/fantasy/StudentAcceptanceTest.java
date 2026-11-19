package edu.calpoly.csc3100.fantasy;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentAcceptanceTest {
  @Test
  void factoryRebuildsWarriorAndEquipmentChainFromPlayerState() {
    PlayerState state = new PlayerState("alice", "warrior", 2, 3,
        List.of("sword", "shield", "shoes"));

    Character character = CharacterFactory.create(state);

    assertAll(
        () -> assertEquals("warrior", character.type()),
        () -> assertInstanceOf(Shoes.class, character),
        () -> assertEquals(15, character.attack()),
        () -> assertEquals(10, character.defense()));
  }

  @Test
  void localEquipmentChangesPublishOnlyDistinctOrderedSnapshots() {
    List<PlayerState> published = new ArrayList<>();
    Blackboard blackboard = new Blackboard(published::add);
    PlayerState initial = new PlayerState("alice", "warrior", 2, 3, List.of());
    blackboard.addLocalPlayer(initial);

    blackboard.equipLocal("alice", "sword");
    blackboard.equipLocal("alice", "sword");
    blackboard.equipLocal("alice", "shield");
    blackboard.unequipLocal("alice", "sword");
    blackboard.unequipLocal("alice", "sword");

    assertEquals(List.of(
        initial,
        new PlayerState("alice", "warrior", 2, 3, List.of("sword")),
        new PlayerState("alice", "warrior", 2, 3, List.of("sword", "shield")),
        new PlayerState("alice", "warrior", 2, 3, List.of("shield"))), published);
    assertEquals(published.getLast(), blackboard.getPlayer("alice"));
  }

  @Test
  void incomingRemoteSnapshotUpdatesBlackboardWithoutRepublishing() {
    List<PlayerState> published = new ArrayList<>();
    Blackboard blackboard = new Blackboard(published::add);
    blackboard.addLocalPlayer(new PlayerState("alice", "warrior", 0, 0, List.of()));
    PlayerState remote = new PlayerState("zoë", "warrior", 15, 10, List.of("shoes"));
    IncomingPlayerHandler handler = new IncomingPlayerHandler("team01", "alice", blackboard::applyRemote);

    handler.accept("csc3100/a04/team01/players/zoë",
        PlayerCodec.encode(remote).getBytes(StandardCharsets.UTF_8));

    assertEquals(remote, blackboard.getPlayer("zoë"));
    assertEquals(List.of(new PlayerState("alice", "warrior", 0, 0, List.of())), published);
  }
}
