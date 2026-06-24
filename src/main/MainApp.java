package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // 1. محاولة قراءة ملف الـ FXML عبر الـ ClassLoader لتفادي الـ NullPointerException
        java.net.URL fxmlLocation = getClass().getClassLoader().getResource("view/Login.fxml");
        
        // إذا فشلت الطريقة الأولى، نجرب المسار المباشر كخيار احتياطي
        if (fxmlLocation == null) {
            fxmlLocation = getClass().getResource("/view/Login.fxml");
        }
        
        // طباعة تشخيصية في الـ Output للتأكد من نجاح العثور على الملف
        if (fxmlLocation == null) {
            System.out.println("❌ ERROR: Login.fxml could not be found anywhere! Check your package name.");
            throw new java.io.FileNotFoundException("Could not find view/Login.fxml");
        } else {
            System.out.println("✅ SUCCESS: Found Login.fxml at: " + fxmlLocation.getPath());
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        // 2. قراءة ملف التنسيق الـ CSS بنفس الطريقة الآمنة
        java.net.URL cssLocation = getClass().getClassLoader().getResource("style.css");
        if (cssLocation == null) {
            cssLocation = getClass().getResource("/style.css");
        }
        
        if (cssLocation != null) {
            String cssPath = cssLocation.toExternalForm();
            scene.getStylesheets().add(cssPath);
            System.out.println("✅ SUCCESS: Loaded style.css");
        } else {
            System.out.println("⚠️ WARNING: style.css not found, running layout without styles.");
        }
        
        // 3. إعدادات وتجهيز نافذة العرض
        primaryStage.setTitle("GHADS - Gaza Humanitarian Aid Distribution System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}