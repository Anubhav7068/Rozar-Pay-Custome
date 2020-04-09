package soft.gen.rozarpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.PaymentResultListener;
import com.razorpay.Razorpay;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText etx_amt;
    Button btn_pay;
    float amount;
    private WebView webview;
    Razorpay razorpay ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        razorpay    = new Razorpay(MainActivity.this , "rzp_test_FibWdhtgn2Hhf2");
        webview = findViewById(R.id.payment_webview);
// Hide the webview until the payment details are submitted
        webview.setVisibility(View.GONE);
        razorpay.setWebView(webview);
        try {
            JSONObject data = new JSONObject();
            data.put("amount", 1000); // pass in currency subunits. For example, paise. Amount: 1000 equals ₹10
            data.put("order_id", "order_DgZ26rHjbzLLY2");//sample order_id. Generate orders using Orders API
            data.put("email", "gaurav.kumar@example.com");
            data.put("contact", "9876543210");
            JSONObject notes = new JSONObject();
            notes.put("custom_field", "abc");
            data.put("notes", notes);
            data.put("method", "netbanking");
            // Method specific fields
            data.put("bank", "HDFC");

            // Make webview visible before submitting payment details
            webview.setVisibility(View.VISIBLE);

            razorpay.submit(data, new PaymentResultListener() {
                @Override
                public void onPaymentSuccess(String razorpayPaymentId) {
                    // Razorpay payment ID is passed here after a successful payment
                }

                @Override
                public void onPaymentError(int code, String description) {
                    // Error code and description is passed here
                }
            });

        } catch (Exception e) {
            Log.e("KS", "Error in submitting payment details", e);
        }

        startPayment();

    }

    private void startPayment() {

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", 50000); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", false);

//            Order order = razorpay.Orders.create(orderRequest);
        } catch ( JSONException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }

        razorpay.getPaymentMethods(new Razorpay.PaymentMethodsCallback() {
            @Override
            public void onPaymentMethodsReceived(String result) {
                try {
                    JSONObject paymentMethods = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
