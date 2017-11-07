package leontrans.leontranstm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SystemServicesUtils {
    public void startDial(Context context, String telephoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephoneNumber));
        context.startActivity(intent);
    }

    public void startInternetBrowser(Context context, String urlAddres){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + urlAddres));
        context.startActivity(intent);
    }

    public void startMaps(Context context, CityCoordinates cityCoordinates){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + cityCoordinates.getLat() + ", " + cityCoordinates.getLng()));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public void startMaps(Context context, String cityName){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0.0?q=" + cityName));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public void startMail(Context context, String mailTo){
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(mailTo) +
                "?subject=" + Uri.encode("leontrans deals") +
                "&body=" + Uri.encode("Please, type text, you want send to this user.");
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        context.startActivity(Intent.createChooser(send, "Choose appropriate program"));
    }
}
