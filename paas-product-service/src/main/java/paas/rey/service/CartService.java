package paas.rey.service;

import paas.rey.request.CartItemRequest;
import paas.rey.utils.JsonData;

public interface CartService {
    void addCartItem(CartItemRequest cartItemRequest);

    void deleteCartItem();

    JsonData getCartItem();
}
