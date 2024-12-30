package com.dyma.tennis.service;

import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerToCreate;
import com.dyma.tennis.model.PlayerToUpdate;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PlayerServiceIntegrationTest {

    @Autowired
    private PlayerService playerService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldCreatePlayer() {
        // Given
        PlayerToCreate playerToCreate = new PlayerToCreate(
                "John",
                "Doe",
                LocalDate.of(2000, Month.JANUARY, 1),
                10000
        );

        // When
        Player savedPlayer = playerService.create(playerToCreate);
        Player createdPlayer = playerService.getByIdentifier(savedPlayer.identifier());

        // Then
        Assertions.assertThat(createdPlayer.firstName()).isEqualTo("John");
        Assertions.assertThat(createdPlayer.lastName()).isEqualTo("Doe");
        Assertions.assertThat(createdPlayer.birthDate()).isEqualTo(LocalDate.of(2000, Month.JANUARY, 1));
        Assertions.assertThat(createdPlayer.rank().points()).isEqualTo(10000);
        Assertions.assertThat(createdPlayer.rank().position()).isEqualTo(1);
    }

    @Test
    public void shouldFailToCreateAnExistingPlayer() {
        // Given
        PlayerToCreate playerToCreate = new PlayerToCreate(
                "John",
                "Doe",
                LocalDate.of(2000, Month.JANUARY, 1),
                10000
        );
        playerService.create(playerToCreate);
        PlayerToCreate duplicatedPlayerToCreate = new PlayerToCreate(
                "John",
                "Doe",
                LocalDate.of(2000, Month.JANUARY, 1),
                12000
        );

        // When / Then
        Exception exception = assertThrows(PlayerAlreadyExistsException.class, () -> {
            playerService.create(duplicatedPlayerToCreate);
        });
        Assertions.assertThat(exception.getMessage()).contains("Player with " +
                "firstName John " +
                "lastName Doe " +
                "and birthDate 2000-01-01 already exists.");
    }

    @Test
    public void shouldUpdatePlayer() {
        // Given
        UUID nadalIdentifier = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        PlayerToUpdate playerToUpdate = new PlayerToUpdate(
                nadalIdentifier,
                "Rafael",
                "NadalTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When
        playerService.update(playerToUpdate);
        Player updatedPlayer = playerService.getByIdentifier(nadalIdentifier);

        // Then
        Assertions.assertThat(updatedPlayer.rank().position()).isEqualTo(3);
    }

    @Test
    public void shouldDeletePlayer() {
        // Given
        UUID djokovicIdentifier = UUID.fromString("d27aef45-51cd-401b-a04a-b78a1327b793");

        // When
        playerService.delete(djokovicIdentifier);

        // Then
        List<Player> allPlayers = playerService.getAllPlayers();
        Assertions.assertThat(allPlayers)
                .extracting("lastName", "rank.position")
                .containsExactly(
                        Tuple.tuple("NadalTest", 1),
                        Tuple.tuple("FedererTest", 2)
                );
    }

    @Test
    public void shouldFailToDeletePlayer_WhenPlayerDoesNotExist() {
        // Given
        UUID playerToDelete = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");

        // When / Then
        Exception exception = assertThrows(PlayerNotFoundException.class, () -> {
            playerService.delete(playerToDelete);
        });
        Assertions.assertThat(exception.getMessage()).isEqualTo("Player with identifier aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb could not be found.");
    }
}
