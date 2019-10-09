package com.example.myapplication.model.task;

import android.os.AsyncTask;

import androidx.arch.core.util.Function;

import com.example.myapplication.model.IctisResponse;
import com.example.myapplication.model.IctisSelectorResponse;
import com.example.myapplication.model.SheduleData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;


public class GetTableAsyncTask extends AsyncTask<RequestData, String, IctisResponse> {

    private final Gson gson = new Gson();

    private final Function<IctisResponse, Boolean> callback;
    IctisResponse dataObject;

    public GetTableAsyncTask(Function<IctisResponse, Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected IctisResponse doInBackground(RequestData... requests) {

        RequestData request = requests[0];
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            httpURLConnection.setRequestMethod(request.getRequestMethod());
            httpURLConnection.setRequestProperty("Content-Type", request.getContentType());
            String response = readResponseToString(new InputStreamReader(httpURLConnection.getInputStream()));
            dataObject = new IctisResponse();
            if (response.contains("choices")){
                dataObject.setIctisSelectorResponse(gson.fromJson(response, IctisSelectorResponse.class));
            }else {
                dataObject.setScheduledData(gson.fromJson(response, SheduleData.class));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return dataObject;
    }

    public static String readResponseToString(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder data = new StringBuilder();
        int inputStreamData = inputStreamReader.read();
        while (inputStreamData != -1) {
            char current = (char) inputStreamData;
            inputStreamData = inputStreamReader.read();
            data.append(current);

        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(IctisResponse result) {
        if (callback != null) {
            try {
                callback.apply(dataObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
