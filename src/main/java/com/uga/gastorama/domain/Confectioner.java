package com.uga.gastorama.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Confectioner.
 */
@Entity
@Table(name = "confectioner")
@Document(indexName = "confectioner")
public class Confectioner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "confectioner", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "confectioner", cascade = {CascadeType.REMOVE,CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Set<ConfectionerPhoto> photos = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Confectioner address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public Confectioner description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Confectioner products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Confectioner addProduct(Product product) {
        this.products.add(product);
        product.setConfectioner(this);
        return this;
    }

    public Confectioner removeProduct(Product product) {
        this.products.remove(product);
        product.setConfectioner(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
/*
    public Set<ConfectionerPhoto> getPhotos() {
        return photos;
    }*/

    public Confectioner photos(Set<ConfectionerPhoto> confectionerPhotos) {
        this.photos = confectionerPhotos;
        return this;
    }

    public Confectioner addPhoto(ConfectionerPhoto confectionerPhoto) {
        this.photos.add(confectionerPhoto);
        confectionerPhoto.setConfectioner(this);
        return this;
    }

    public Confectioner removePhoto(ConfectionerPhoto confectionerPhoto) {
        this.photos.remove(confectionerPhoto);
        confectionerPhoto.setConfectioner(null);
        return this;
    }

    public void setPhotos(Set<ConfectionerPhoto> confectionerPhotos) {
        this.photos = confectionerPhotos;
    }

    public User getUserId() {
        return userId;
    }

    public Confectioner userId(User user) {
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
        Confectioner confectioner = (Confectioner) o;
        if (confectioner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confectioner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Confectioner{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
