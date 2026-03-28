package com.youkang.system.utils;

/**
 * 孔号工具类
 *
 * @author youkang
 */
public class HoleNoUtils {

    /**
     * 行字母数组
     */
    private static final String[] ROWS = {"A", "B", "C", "D", "E", "F", "G", "H"};

    /**
     * 总列数
     */
    private static final int COLS = 12;

    /**
     * 总行数
     */
    private static final int ROW_COUNT = 8;

    /**
     * 根据孔号和排版方式计算孔号数（1-96）
     *
     * @param holeNo   孔号，如 "A01", "B12", "H08"
     * @param layout   排版方式："横排" 或 "竖排"
     * @return 孔号数（1-96），如果孔号无效返回 null
     */
    public static Integer calculateHoleNumber(String holeNo, String layout) {
        if (holeNo == null || holeNo.isEmpty()) {
            return null;
        }

        // 解析孔号
        if (holeNo.length() < 2 || holeNo.length() > 3) {
            return null;
        }

        String rowStr = holeNo.substring(0, 1).toUpperCase();
        String colStr = holeNo.substring(1);

        // 查找行索引（0-7）
        int rowIndex = -1;
        for (int i = 0; i < ROWS.length; i++) {
            if (ROWS[i].equals(rowStr)) {
                rowIndex = i;
                break;
            }
        }
        if (rowIndex == -1) {
            return null;
        }

        // 解析列号（1-12）
        int colIndex;
        try {
            colIndex = Integer.parseInt(colStr);
        } catch (NumberFormatException e) {
            return null;
        }
        if (colIndex < 1 || colIndex > COLS) {
            return null;
        }

        // 根据排版方式计算孔号数
        if ("横排".equals(layout)) {
            // 横排：A01=1, A02=2, ..., A12=12, B01=13, ...
            return rowIndex * COLS + colIndex;
        } else if ("竖排".equals(layout)) {
            // 竖排：A01=1, B01=2, ..., H01=8, A02=9, ...
            return (colIndex - 1) * ROW_COUNT + rowIndex + 1;
        }

        return null;
    }

    /**
     * 验证孔号是否有效
     *
     * @param holeNo 孔号
     * @return 是否有效
     */
    public static boolean isValidHoleNo(String holeNo) {
        if (holeNo == null || holeNo.isEmpty()) {
            return false;
        }

        if (holeNo.length() < 2 || holeNo.length() > 3) {
            return false;
        }

        String rowStr = holeNo.substring(0, 1).toUpperCase();
        String colStr = holeNo.substring(1);

        // 验证行
        boolean validRow = false;
        for (String row : ROWS) {
            if (row.equals(rowStr)) {
                validRow = true;
                break;
            }
        }
        if (!validRow) {
            return false;
        }

        // 验证列
        try {
            int colIndex = Integer.parseInt(colStr);
            return colIndex >= 1 && colIndex <= COLS;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
