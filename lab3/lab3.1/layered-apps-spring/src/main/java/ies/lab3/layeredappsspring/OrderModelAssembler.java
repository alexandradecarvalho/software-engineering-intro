package ies.lab3.layeredappsspring;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order,EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order){
        EntityModel<Order> orderModel = EntityModel.of(order,linkTo(methodOn(OrderController.class).all()).withRel("orders"));

        if (order.getStatus().equals(Status.IN_PROGRESS)){
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }
        return orderModel;
    }
}
