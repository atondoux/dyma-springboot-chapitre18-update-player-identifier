package com.dyma.tennis.data;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "player", schema = "public")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "identifier", length = 100, nullable = false, unique = true)
    private String identifier;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    public PlayerEntity() {
    }

    public PlayerEntity(String lastName, String firstName, LocalDate birthDate, Integer points, Integer rank) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = generateIdentifier();
        this.birthDate = birthDate;
        this.points = points;
        this.rank = rank;
    }

    public PlayerEntity(Long id, String lastName, String firstName, LocalDate birthDate, Integer points, Integer rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = generateIdentifier();
        this.birthDate = birthDate;
        this.points = points;
        this.rank = rank;
    }

    private String generateIdentifier() {
        return (this.firstName + this.lastName)
                .toLowerCase()
                .transform(s -> s.replaceAll("[^a-z0-9]", ""));
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.identifier = generateIdentifier();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.identifier = generateIdentifier();
    }

    public String getIdentifier() {
        return identifier;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
