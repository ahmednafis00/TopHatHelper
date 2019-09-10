// created by Nafis Ahmed on 1st Dec 2018

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.*;

public class TopHatHelper {

    private static final String subscriptionKey = " "; // Azure subscription key

    private static final String uriBase =
            "https://eastus.api.cognitive.microsoft.com/face/v1.0/detect";

    public static void main(String[] args) {
        HttpClient httpclient = new DefaultHttpClient();

        try {
            Scanner scnr = new Scanner(System.in);

            String imageWithFaces = "{\"url\":\"";
            String end = "\"}";

            System.out.println("Enter photo no.");
            int photo = scnr.nextInt();

            // Images used for demonstration
            switch (photo) {
                case 1:
                    imageWithFaces += "https://image.shutterstock.com/image-photo/group-women-socialize-teamwork-happiness-450w-526817902.jpg" + end;
                    break;
                case 2:
                    imageWithFaces += "https://nguyenresearchgroup.lab.uiowa.edu/sites/nguyenresearchgroup.lab.uiowa.edu/files/styles/featured_slider/public/featured_slider/Group2016cropped-1.jpg?itok=stbrVL2x&c=b89b62d795750b1960468e13b4a971ae&fbclid=IwAR3iM0iWGuHi_1fhS4KaKphjA2z-iv1vJILkweZEre1T_yxORyxzI8lMri4" + end;
                    break;
                case 3:
                    imageWithFaces += "https://cdn2.hubspot.net/hubfs/502949/Blogs/SH-blog-high-school-rank.jpg" + end;
                    break;
                default:
                    imageWithFaces += "https://2o34683axl001lupaf2udyvl-wpengine.netdna-ssl.com/wp-content/uploads/SoftWhite_Edited_2018.jpg" + end;
                    break;
            }

            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters
            builder.setParameter("returnFaceId", "true");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity(imageWithFaces);
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                String jsonStr = jsonString.replace("faceId", "ID");

                // Counts the number of faces.
                int count = 0;
                for (int i = 0; i < jsonStr.length(); ++i) {
                    if (jsonStr.charAt(i) == 'I' && jsonStr.charAt(i + 1) == 'D') {
                        ++count;
                    }
                }

                System.out.println("Faces detected: " + count);
                System.out.println("Enter the number of attendees from TopHat:");
                int ans = scnr.nextInt();

                // Compares against TopHat.
                int diff = ans - count;
                if (diff > 0) {
                    System.out.println(diff + " student(s) wrongly marked themselves present.");
                } else if (diff < 0) {
                    System.out.println("Face detection was not performed accurately. Try using a clearer image.");
                } else {
                    System.out.println("Everyone was rightly marked present.");
                }
            }

        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}