package ua.pt.naturespot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyObservations.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyObservations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyObservations extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Observation> lstBook ;
    private OnFragmentInteractionListener mListener;
    RecyclerViewAdapter myAdapter = null;


    public MyObservations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyObservations.
     */
    // TODO: Rename and change types and number of parameters
    public static MyObservations newInstance(String param1, String param2) {
        MyObservations fragment = new MyObservations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }







    }

    private String[] getScreenDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        String[] screenInformation = new String[3];
        screenInformation[0] = String.valueOf(width) + " px";
        screenInformation[1] = String.valueOf(height) + " px" ;
        screenInformation[2] = String.format("%.2f", screenInches) + " inches" ;

        return screenInformation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lstBook = new ArrayList<>();
        lstBook.add(new Observation("The Vegitarian","Categorie Book","Description book",R.drawable.thevigitarian));
        lstBook.add(new Observation("The Wild Robot","Categorie Book","Description book",R.drawable.thewildrobot));
        lstBook.add(new Observation("Maria Semples","Categorie Book","Description book",R.drawable.mariasemples));







        View view = inflater.inflate(R.layout.fragment_my_observations, container, false);
        RecyclerView myrv = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        myrv.setLayoutManager(manager);
        myAdapter = new RecyclerViewAdapter(this.getContext(),lstBook);
        myrv.setAdapter(myAdapter);
        return inflater.inflate(R.layout.fragment_my_observations, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
