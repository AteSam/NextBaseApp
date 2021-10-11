package com.example.nextbaseapp.MapView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nextbaseapp.Mvvm.DataViewModel
import com.example.nextbaseapp.Mvvm.DataViewModelFactory
import com.example.nextbaseapp.R
import com.example.nextbaseapp.model.Data
import com.example.nextbaseapp.model.Journey_model
import com.example.nextbaseapp.repo.Repository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MapViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapViewFragment : Fragment() {

    private lateinit var mMap : GoogleMap
    private var mapReady = false

    @Inject
    lateinit var repository: Repository

    private val dataViewModelFactory by lazy {
        DataViewModelFactory(repository)
    }

    private val viewmodel by lazy {
        ViewModelProvider(this, dataViewModelFactory)
            .get(DataViewModel::class.java)
    }


    private lateinit var dataList: List<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_map_view, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
            updateMap()
        }
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewmodel.ResponseList.observe(this.viewLifecycleOwner,object : Observer<List<Journey_model>> {
            override fun onChanged(t: List<Journey_model>?) {

                if (t != null) {
                    for (i in 0 until t.size) {
                        dataList =t.get(i).data
                        updateMap()
                    }
                }
            }
        })

    }


    internal fun updateMap() {
        if (mapReady && dataList != null) {
            dataList.forEach {
                    data ->
                if (data.lon != null && data.lat != null) {
                    val marker = LatLng(data.lat.toDouble(), data.lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(marker).title(data.toString()))
                }

            }
        }
    }


}


