package org.grp2.domain;

public class BatchViewOrder {
    private int batchId;
    private int amountToProduce;
    private int productsPerMinute;

    public BatchViewOrder(int batchId, int amountToProduce, int productsPerMinute) {
        this.batchId = batchId;
        this.amountToProduce = amountToProduce;
        this.productsPerMinute = productsPerMinute;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getAmountToProduce() {
        return amountToProduce;
    }

    public void setAmountToProduce(int amountToProduce) {
        this.amountToProduce = amountToProduce;
    }

    public int getProductsPerMinute() {
        return productsPerMinute;
    }

    public void setProductsPerMinute(int productsPerMinute) {
        this.productsPerMinute = productsPerMinute;
    }
}
