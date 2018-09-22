package com.uga.gastorama.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductType.
 */
@Entity
@Table(name = "product_type")
@Document(indexName = "producttype")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "productType", cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductTypePhoto> photos = new HashSet<>();

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

    public ProductType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public ProductType products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public ProductType addProduct(Product product) {
        this.products.add(product);
        product.setProductType(this);
        return this;
    }

    public ProductType removeProduct(Product product) {
        this.products.remove(product);
        product.setProductType(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<ProductTypePhoto> getPhotos() {
        return photos;
    }

    public ProductType photos(Set<ProductTypePhoto> productTypePhotos) {
        this.photos = productTypePhotos;
        return this;
    }

    public ProductType addPhoto(ProductTypePhoto productTypePhoto) {
        this.photos.add(productTypePhoto);
        productTypePhoto.setProductType(this);
        return this;
    }

    public ProductType removePhoto(ProductTypePhoto productTypePhoto) {
        this.photos.remove(productTypePhoto);
        productTypePhoto.setProductType(null);
        return this;
    }

    public void setPhotos(Set<ProductTypePhoto> productTypePhotos) {
        this.photos = productTypePhotos;
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
        ProductType productType = (ProductType) o;
        if (productType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
