package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CommandLine.
 */
@Entity
@Table(name = "command_line")
@Document(indexName = "commandline")
public class CommandLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties("commandLines")
    private Product product;


    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonIgnoreProperties("commandLines")
    private Command command;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CommandLine quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public CommandLine product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Command getCommand() {
        return command;
    }

    public CommandLine command(Command command) {
        this.command = command;
        return this;
    }

    public void setCommand(Command command) {
        this.command = command;
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
        CommandLine commandLine = (CommandLine) o;
        if (commandLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commandLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommandLine{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
