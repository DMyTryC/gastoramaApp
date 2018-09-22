package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.uga.gastorama.domain.enumeration.DeliveryMode;

import com.uga.gastorama.domain.enumeration.State;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
@Document(indexName = "command")
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sumprice", nullable = false)
    private Integer sumprice;

    @NotNull
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery", nullable = false)
    private DeliveryMode delivery;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @OneToMany(mappedBy = "command", fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CommandLine> commandLines = new HashSet<>();

    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonIgnore
    private User userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSumprice() {
        return sumprice;
    }

    public Command sumprice(Integer sumprice) {
        this.sumprice = sumprice;
        return this;
    }

    public void setSumprice(Integer sumprice) {
        this.sumprice = sumprice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Command deliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Instant getDate() {
        return date;
    }

    public Command date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public DeliveryMode getDelivery() {
        return delivery;
    }

    public Command delivery(DeliveryMode delivery) {
        this.delivery = delivery;
        return this;
    }

    public void setDelivery(DeliveryMode delivery) {
        this.delivery = delivery;
    }

    public State getState() {
        return state;
    }

    public Command state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<CommandLine> getCommandLines() {
        return commandLines;
    }

    public Command commandLines(Set<CommandLine> commandLines) {
        this.commandLines = commandLines;
        return this;
    }

    public Command addCommandLine(CommandLine commandLine) {
        this.commandLines.add(commandLine);
        commandLine.setCommand(this);
        return this;
    }

    public Command removeCommandLine(CommandLine commandLine) {
        this.commandLines.remove(commandLine);
        commandLine.setCommand(null);
        return this;
    }

    public void setCommandLines(Set<CommandLine> commandLines) {
        this.commandLines = commandLines;
    }

    public User getUserId() {
        return userId;
    }

    public Command userId(User user) {
        this.userId = user;
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        if (command.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), command.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", sumprice=" + getSumprice() +
            ", deliveryAddress='" + getDeliveryAddress() + "'" +
            ", date='" + getDate() + "'" +
            ", delivery='" + getDelivery() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
