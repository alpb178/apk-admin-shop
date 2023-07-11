package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class Data implements Serializable {

    private JewelSalePost data;

    public Data(JewelSalePost data) {
        this.data = data;
    }

    public static class DataUpdate implements Serializable {

        private DataUpdateJewel data;

        public DataUpdate(DataUpdateJewel data) {
            this.data = data;
        }
    }

    public static class DataUpdateJewel implements Serializable {

        private int availability;

        public DataUpdateJewel(int availability) {
            this.availability = availability;
        }
    }

    public static class DataUpdateJewelSales implements Serializable {

        private DataUpdateJewelSale data;

        public DataUpdateJewelSales(DataUpdateJewelSale data) {
            this.data = data;
        }
    }

    public static class DataUpdateJewelSale implements Serializable {

        private String status;

        public DataUpdateJewelSale(String status) {
            this.status = status;
        }
    }

    public static class DataCreateJewel implements Serializable {

        private String status;

        public DataCreateJewel(String status) {
            this.status = status;
        }
    }
}





