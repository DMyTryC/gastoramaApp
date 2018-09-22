package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Proxy;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Document(indexName = "product")
@Proxy(lazy=false)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "pieces")
    private Integer pieces;

    @NotNull
    @Column(name = "pass_date", nullable = false)
    private Instant passDate;

    @Column(name = "stock")
    private Integer stock;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProductPhoto> photos = new HashSet<>();

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("products")
    private Confectioner confectioner;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("products")
    private ProductType productType;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<CommandLine> commandLines = new HashSet<>();

    @ManyToMany(mappedBy = "products", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ingredient> ingredients = new HashSet<>();

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

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public Product price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWeight() {
        return weight;
    }

    public Product weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getPieces() {
        return pieces;
    }

    public Product pieces(Integer pieces) {
        this.pieces = pieces;
        return this;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public Instant getPassDate() {
        return passDate;
    }

    public Product passDate(Instant passDate) {
        this.passDate = passDate;
        return this;
    }

    public void setPassDate(Instant passDate) {
        this.passDate = passDate;
    }

    public Integer getStock() {
        return stock;
    }

    public Product stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Set<ProductPhoto> getPhotos() {
        return photos;
    }

    public Product photos(Set<ProductPhoto> productPhotos) {
        this.photos = productPhotos;
        return this;
    }

    public Product addPhoto(ProductPhoto productPhoto) {
        this.photos.add(productPhoto);
        productPhoto.setProduct(this);
        return this;
    }

    public Product removePhoto(ProductPhoto productPhoto) {
        this.photos.remove(productPhoto);
        productPhoto.setProduct(null);
        return this;
    }

    public void setPhotos(Set<ProductPhoto> productPhotos) {
        this.photos = productPhotos;
    }

    public Confectioner getConfectioner() {
        return confectioner;
    }

    public Product confectioner(Confectioner confectioner) {
        this.confectioner = confectioner;
        return this;
    }

    public void setConfectioner(Confectioner confectioner) {
        this.confectioner = confectioner;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Product productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Set<CommandLine> getCommandLines() {
        return commandLines;
    }

    public Product commandLines(Set<CommandLine> commandLines) {
        this.commandLines = commandLines;
        return this;
    }

    public Product addCommandLine(CommandLine commandLine) {
        this.commandLines.add(commandLine);
        commandLine.setProduct(this);
        return this;
    }

    public Product removeCommandLine(CommandLine commandLine) {
        this.commandLines.remove(commandLine);
        commandLine.setProduct(null);
        return this;
    }

    public void setCommandLines(Set<CommandLine> commandLines) {
        this.commandLines = commandLines;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Product ingredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Product addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getProducts().add(this);
        return this;
    }

    public Product removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getProducts().remove(this);
        return this;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", weight=" + getWeight() +
            ", pieces=" + getPieces() +
            ", passDate='" + getPassDate() + "'" +
            ", stock=" + getStock() +
            "}";
    }

}
