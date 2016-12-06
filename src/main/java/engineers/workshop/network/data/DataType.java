package engineers.workshop.network.data;

import engineers.workshop.network.DataReader;
import engineers.workshop.network.DataWriter;
import engineers.workshop.network.IBitCount;
import engineers.workshop.network.LengthCount;
import engineers.workshop.table.TileTable;

public enum DataType {
    PAGE(DataPage.class),
    PROGRESS(DataUnit.Progress.class, DataUnit.LENGTH),
    POWER(DataFuel.class),
    SIDE_ENABLED(DataSide.Enabled.class, DataSide.LENGTH),
    SIDE_AUTO(DataSide.Auto.class, DataSide.LENGTH),
    SIDE_FILTER(DataSide.Filter.class, DataSide.FilterBase.LENGTH),
    SIDE_WHITE_LIST(DataSide.WhiteList.class, DataSide.LENGTH),
    SIDE_FILTER_MODE(DataSide.FilterMode.class, DataSide.FilterBase.LENGTH),
    LAVA(DataLava.class),
    LIT(DataLit.class),
    CHARGED(DataUnit.Charged.class, DataUnit.LENGTH);

    private IBitCount lengthBits;
    private int length;
    private DataBase data;

    DataType(Class<? extends DataBase> clazz, int length) {
        this(clazz);
        this.length = length;
        lengthBits = new LengthCount(length);
    }

    DataType(Class<? extends DataBase> clazz) {
        try {
            data = clazz.newInstance();
        }catch (Exception e) {
            e.printStackTrace();
        }
        this.length = 1;
    }

    public void save(TileTable table, DataWriter dw, int id) {
        if (data != null) {
            if (id == -1) {
                for (int i = 0; i < length; i++) {
                    data.save(table, dw, i);
                }
            }else{
                if (lengthBits != null) {
                    dw.writeData(id, lengthBits);
                }
                data.save(table, dw, id);
            }
        }
    }

    public int load(TileTable table, DataReader dr, boolean all) {
        if (data != null) {
            if (all) {
                for (int i = 0; i < length; i++) {
                    data.load(table, dr, i);
                }
            }else{
                int id = 0;
                if (lengthBits != null) {
                    id = dr.readData(lengthBits);
                }
                data.load(table, dr, id);
                return id;
            }
        }

        return -1;
    }

    public boolean shouldBounce(TileTable table) {
        return data != null && data.shouldBounce(table);
    }

    public boolean shouldBounceToAll(TileTable table) {
        return data != null && data.shouldBounceToAll(table);
    }
}