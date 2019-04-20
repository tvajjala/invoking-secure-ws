package com.tvajjala.secure.ws.model;

import java.time.Instant;
import java.util.Objects;

public class Order {

    private Integer id;

    private Integer quantity;

    private Integer petId;


    private Instant shipDate;

    private boolean completed;

    private String status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Instant getShipDate() {
        return shipDate;
    }

    public void setShipDate(Instant shipDate) {
        this.shipDate = shipDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", petId=" + petId +
                ", shipDate=" + shipDate +
                ", completed=" + completed +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return completed == order.completed &&
                Objects.equals(id, order.id) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(petId, order.petId) &&
                Objects.equals(shipDate, order.shipDate) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, quantity, petId, shipDate, completed, status);
    }
}
