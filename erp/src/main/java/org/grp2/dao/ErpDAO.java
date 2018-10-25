package org.grp2.dao;

import jdk.internal.jline.internal.Nullable;
import org.grp2.database.DatabaseConnection;
import org.grp2.domain.OrderItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ErpDAO extends DatabaseConnection {

    /**
     * Creates a new order and returns the newly created order_number.
     *
     * @return order_number-
     */
    public int getNewOrderNumber() {
        AtomicReference<BigDecimal> orderID = new AtomicReference<>();

        this.executeQuery(conn -> {
            String insertOrderQuery = "INSERT INTO Orders VALUES(DEFAULT, DEFAULT, DEFAULT) RETURNING order_number";
            PreparedStatement ps = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderID.set(rs.getBigDecimal(1));
            }
            ps.close();
        });

        return orderID.get().intValue();
    }

    /**
     * @param orderNumber
     * @param beerName
     * @param quantity
     * @return
     */
    public boolean addOrderItem(int orderNumber, String beerName, int quantity) {

        if (doesOrderExist(orderNumber) && !isDuplicateOrderItem(orderNumber, beerName)) {
            this.executeQuery(conn -> {
                try {
                    String insertOrderItemQuery = "INSERT INTO Order_items (quantity, order_number, status, beer_name) VALUES (?, ?, default, ?);";
                    PreparedStatement ps = conn.prepareStatement(insertOrderItemQuery);
                    ps.setInt(1, quantity);
                    ps.setInt(2, orderNumber);
                    ps.setString(3, beerName);
                    ps.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        return false;

    }

    /**
     * @param orderNumber
     * @param beerName
     * @return
     */
    public boolean isDuplicateOrderItem(int orderNumber, String beerName) {
        AtomicInteger flag = new AtomicInteger();

        this.executeQuery(conn -> {
            try {
                String checkIfOrderExistsQuery = "SELECT 1 FROM Order_items WHERE order_number = ? AND beer_name = ?";
                PreparedStatement ps1 = conn.prepareStatement(checkIfOrderExistsQuery);
                ps1.setInt(1, orderNumber);
                ps1.setString(2, beerName);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    flag.set(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        return flag.get() == 1;
    }

    /**
     * @param orderNumber
     * @return
     */
    public boolean doesOrderExist(int orderNumber) {
        AtomicInteger flag = new AtomicInteger();

        this.executeQuery(conn -> {
            try {
                String checkIfOrderExistsQuery = "SELECT 1 FROM Orders WHERE order_number = ?";
                PreparedStatement ps1 = conn.prepareStatement(checkIfOrderExistsQuery);
                ps1.setInt(1, orderNumber);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    flag.set(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        return flag.get() == 1;
    }


    /**
     * @param orderNumber
     * @return
     */
    public boolean isOrderProcessed(int orderNumber) {
        AtomicReference<String> status = new AtomicReference<>();
        this.executeQuery(conn -> {
            String getStatusQuery = "SELECT status FROM Orders WHERE order_number = ?";
            PreparedStatement ps = conn.prepareStatement(getStatusQuery);
            ps.setInt(1, orderNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status.set(rs.getString(1));
            }
        });

        if (status.get().equals("nonProcessed")) return false;
        else return true;
    }

    /**
     * @param orderNumber
     * @return
     */
    public boolean deleteOrder(int orderNumber) {

        boolean isOrderProcessed = isOrderProcessed(orderNumber);

        if (!isOrderProcessed) {
            this.executeQuery(conn -> {
                try {
                    String deleteOrderQuery = "DELETE FROM Orders WHERE order_number = ?";
                    PreparedStatement ps = conn.prepareStatement(deleteOrderQuery);
                    ps.setInt(1, orderNumber);
                    ps.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            return true;

        }

        return false;

    }


    public List<OrderItem> viewOrder(int orderNumber) {
        List<OrderItem> orderItems = new ArrayList<>();

        if (doesOrderExist(orderNumber)) {
            this.executeQuery(conn -> {
                try {
                    String getOrderQuery = "SELECT Order_items.order_number, Order_items.beer_name, Order_items.quantity, Order_items.status " +
                            "FROM Orders, Order_items WHERE Orders.order_number = ? AND Orders.order_number = Order_items.order_number";
                    PreparedStatement ps = conn.prepareStatement(getOrderQuery);
                    ps.setInt(1, orderNumber);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        int orderItemNumber = rs.getInt(1);
                        String beerName = rs.getString(2);
                        int quantity = rs.getInt(3);
                        String status = rs.getString(4);

                        OrderItem orderItem = new OrderItem(orderItemNumber, beerName, quantity, status);
                        orderItems.add(orderItem);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        return orderItems;

    }

    public boolean deleteOrderItem(int orderNumber, String beerName) {
        boolean isOrderProcessed = isOrderProcessed(orderNumber);

        if (!isOrderProcessed) {
            this.executeQuery(conn -> {
                try {
                    String deleteOrderItemQuery = "DELETE FROM Order_items WHERE order_number = ? AND beer_name = ?";
                    PreparedStatement ps = conn.prepareStatement(deleteOrderItemQuery);
                    ps.setInt(1, orderNumber);
                    ps.setString(2, beerName);
                    ps.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            return true;

        }

        return false;
    }

    public boolean editOrderItem(int orderNumber, String beerName, @Nullable Integer quantity, @Nullable String newBeerName){
        boolean isOrderProcessed = isOrderProcessed(orderNumber);

        if (!isOrderProcessed) {
            this.executeQuery(conn -> {
                try {
                    String updateOrderItemQuery = "UPDATE Order_items SET quantity = ?, beer_name = ? WHERE order_number = ? AND beer_name = ?";
                    PreparedStatement ps = conn.prepareStatement(updateOrderItemQuery);

                    if(quantity == null){
                        ps.setString(1, "(SELECT Order_items.quantity FROM Order_items WHERE order_number " + orderNumber + " AND beer_name = " + beerName + ")");
                    } else{
                        ps.setInt(1, quantity.intValue());
                    }

                    if(newBeerName == null){
                        ps.setString(2, "(SELECT Order_items.beer_name FROM Order_items WHERE order_number " + orderNumber + " AND beer_name = " + beerName + ")");
                    } else{
                        ps.setString(2, newBeerName);
                    }

                    ps.setInt(3,orderNumber);
                    ps.setString(4, beerName);

                    ps.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });

            return true;
        }

        return false;
    }

}
