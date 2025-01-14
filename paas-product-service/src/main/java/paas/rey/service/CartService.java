package paas.rey.service;

import paas.rey.model.CartItemMessage;
import paas.rey.request.CartItemLockRequest;
import paas.rey.request.CartItemRequest;
import paas.rey.utils.JsonData;

import java.util.List;

public interface CartService {
    void addCartItem(CartItemRequest cartItemRequest);

    void clearCartItem();

    JsonData getCartItem();

    void deleteCartItem(long productId);

    void changeCartItem(CartItemRequest cartItemRequest);

    JsonData confirmOrderCartItem(List<Long> productId);

    JsonData lockCartItem(CartItemLockRequest cartItemLockRequest);

    Boolean releaseCartItem(CartItemMessage cartItemMessage);

}
