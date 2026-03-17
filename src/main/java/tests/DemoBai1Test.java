package tests;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DemoBai1Test extends BaseTest {

    @Test
    public void testChayThuLan1() {
        System.out.println("Test 1 đang chạy trên luồng: " + Thread.currentThread().getId());
        Assert.assertTrue(true); // Test này cho pass
    }

    @Test
    public void testChayThuLan2() {
        System.out.println("Test 2 đang chạy trên luồng: " + Thread.currentThread().getId());
        Assert.assertTrue(true); // Test này cho pass
    }

    @Test
    public void testCoTinhFailDeChupManHinh() {
        System.out.println("Test 3 đang chạy trên luồng: " + Thread.currentThread().getId());
        // Cố tình đánh rớt để kích hoạt cơ chế chụp ảnh màn hình của BaseTest
        Assert.fail("Cố tình báo lỗi để xem ảnh màn hình có được lưu không nè!");
    }
}