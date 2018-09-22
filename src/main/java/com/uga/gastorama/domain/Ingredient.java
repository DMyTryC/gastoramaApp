package com.uga.gastorama.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ingredient.
 */
@Entity
@Table(name = "ingredient")
@Document(indexName = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "allergen", nullable = false)
    private Boolean allergen;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "ingredient_product",
               joinColumns = @JoinColumn(name = "ingredients_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "products_id", referencedColumnName = "id"))
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Ingredient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAllergen() {
        return allergen;
    }

    public Ingredient allergen(Boolean allergen) {
        this.allergen = allergen;
        return this;
    }

    public void setAllergen(Boolean allergen) {
        this.allergen = allergen;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Ingredient products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Ingredient addProduct(Product product) {
        this.products.add(product);
        product.getIngredients().add(this);
        return this;
    }

    public Ingredient removeProduct(Product product) {
        this.products.remove(product);
        product.getIngredients().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
        Ingredient ingredient = (Ingredient) o;
        if (ingredient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", allergen='" + isAllergen() + "'" +
            "}";
    }
}
