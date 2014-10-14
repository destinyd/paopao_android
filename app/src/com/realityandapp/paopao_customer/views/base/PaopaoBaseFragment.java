package com.realityandapp.paopao_customer.views.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.realityandapp.paopao_customer.R;
import roboguice.fragment.RoboFragment;

/**
 * Created by dd on 14-10-14.
 */
public class PaopaoBaseFragment extends RoboFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void setTitle(CharSequence title) {
        ((TextView) getActivity().findViewById(R.id.title)).setText(title);
    }

    public void setTitle(int titleId) {
        ((TextView) getActivity().findViewById(R.id.title)).setText(titleId);
    }
}