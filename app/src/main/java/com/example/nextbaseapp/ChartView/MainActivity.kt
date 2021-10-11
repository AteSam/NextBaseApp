package com.example.nextbaseapp.ChartView

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nextbaseapp.ChartView.custom.ChartViewBase
import com.example.nextbaseapp.ChartView.custom.MyMarkerView
import com.example.nextbaseapp.Constants.Constants.xAcc_string
import com.example.nextbaseapp.Constants.Constants.yAcc_string
import com.example.nextbaseapp.Constants.Constants.zAcc_string
import com.example.nextbaseapp.MapView.MapViewFragment
import com.example.nextbaseapp.Mvvm.DataViewModel
import com.example.nextbaseapp.Mvvm.DataViewModelFactory
import com.example.nextbaseapp.MyNextBaseApp
import com.example.nextbaseapp.R
import com.example.nextbaseapp.model.Data
import com.example.nextbaseapp.model.Journey_model
import com.example.nextbaseapp.repo.Repository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import java.util.*
import javax.inject.Inject

class MainActivity : ChartViewBase(), OnChartValueSelectedListener {

    @Inject
    lateinit var repository: Repository

    private val dataViewModelFactory by lazy {
        DataViewModelFactory(repository)
    }

    private val viewmodel by lazy {
        ViewModelProvider(this, dataViewModelFactory)
            .get(DataViewModel::class.java)
    }

    private var chart: LineChart? = LineChart(this)

    private lateinit var save: Button

    private lateinit var map :Button


    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as MyNextBaseApp).appComponent.injectMainActivity(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "ChartView"

        chart = findViewById(R.id.lineChartView)

        initChart()

        viewmodel.ResponseList.observe(this,object : Observer<List<Journey_model>>{
            override fun onChanged(t: List<Journey_model>?) {

                if (t != null) {
                    for (i in 0 until t.size) {

                        setData(t.get(i).data)
                    }
                }
            }
        })



    }

    private fun onClickListner(){
        save = findViewById(R.id.Save)
        map  = findViewById(R.id.map)

        save.setOnClickListener {
            actionSave()
        }

        map.setOnClickListener {
           startMapIntent()
        }

    }




    internal fun  startMapIntent() {
        val intent = Intent(this, MapViewFragment::class.java)
        startActivity(intent)
    }



    private fun initChart(){

        run {   // // Chart Style // //

            // background color
            chart?.setBackgroundColor(Color.WHITE)

            // disable description text
            chart?.getDescription()!!.isEnabled = false

            // enable touch gestures
            chart?.setTouchEnabled(true)

            // set listeners
            chart?.setOnChartValueSelectedListener(this)
            chart?.setDrawGridBackground(false)

            // create marker to display box when values are selected
            val mv = MyMarkerView(this, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.setChartView(chart)
            chart?.setMarker(mv)

            // enable scaling and dragging
            chart?.setDragEnabled(true)
            chart?.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart?.setPinchZoom(true)
        }

        var xAxis: XAxis
        run{   // // X-Axis Style // //
            xAxis = chart!!.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
        }

        var yAxis: YAxis
        run {   // // Y-Axis Style // //
            yAxis = chart!!.axisLeft

            // disable dual axis (only use LEFT axis)
            chart!!.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis.axisMaximum = 200f
            yAxis.axisMinimum = -50f
        }


        run {   // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.lineWidth = 4f
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f
            llXAxis.typeface = tfRegular
            val ll1 = LimitLine(150f, "Upper Limit")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 10f
            ll1.typeface = tfRegular
            val ll2 = LimitLine(-30f, "Lower Limit")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 10f
            ll2.typeface = tfRegular

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)

            // add limit lines
            yAxis.addLimitLine(ll1)
            yAxis.addLimitLine(ll2)
            //xAxis.addLimitLine(llXAxis);
        }

    }




    private fun setData(Data: List<Data>) {


        val xAcc = ArrayList<Entry>()

        val yAcc = ArrayList<Entry>()

        val zAcc = ArrayList<Entry>()



        for (i in 0 until Data.size) {

            val valxAcc= Data.get(i).xAcc
            Log.d("xAcc", valxAcc.toString())

            val valyAcc = Data.get(i).yAcc
            Log.d("yAcc", valyAcc.toString())

            val valzAcc = Data.get(i).zAcc
            Log.d("zAcc", valzAcc.toString())

            xAcc.add(Entry(i.toFloat(), valxAcc))
            yAcc.add(Entry(i.toFloat(), valyAcc))
            zAcc.add(Entry(i.toFloat(), valzAcc))

           xAcc.mapIndexed { index, arrayList ->
                Entry(i.toFloat(), valxAcc) }

           yAcc.mapIndexed { index, arrayList ->
                Entry(i.toFloat(), valyAcc) }

            zAcc.mapIndexed { index, entry ->
                Entry(i.toFloat(),valzAcc)
            }

        }

        val setxAcc: LineDataSet

        val setyAcc: LineDataSet

        val setzAcc: LineDataSet

        if (chart!!.data != null &&
            chart!!.data.dataSetCount > 0
        ) {
            setxAcc = chart!!.data.getDataSetByIndex(0) as LineDataSet
            setyAcc = chart!!.data.getDataSetByIndex(0) as LineDataSet
            setzAcc= chart!!.data.getDataSetByIndex(0) as LineDataSet

            setxAcc.values = xAcc
            setyAcc.values = yAcc
            setzAcc.values = zAcc

            setxAcc.notifyDataSetChanged()
            setyAcc.notifyDataSetChanged()
            setzAcc.notifyDataSetChanged()

            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()

        } else {

            // create a dataset and give it a type
            setxAcc = LineDataSet(xAcc, xAcc_string)
            setyAcc = LineDataSet(yAcc, yAcc_string)
            setzAcc = LineDataSet(zAcc, zAcc_string)

            setxAcc.setDrawIcons(false)
            setyAcc.setDrawIcons(false)
            setzAcc.setDrawIcons(false)


            // draw dashed line
            setxAcc.enableDashedLine(10f, 5f, 0f)
            setyAcc.enableDashedLine(10f, 5f, 0f)
            setzAcc.enableDashedLine(10f, 5f, 0f)


            // blue lines and points
            setxAcc.setColor(Color.BLUE)
            setxAcc.setCircleColor(Color.BLUE)


            // Yellow lines and points
            setyAcc.color = Color.YELLOW
            setyAcc.setCircleColor(Color.YELLOW)


            //Green lines and points
            setzAcc.setColor(Color.GREEN)
            setzAcc.setCircleColor(Color.GREEN)



            // line thickness and point size
            setxAcc.setLineWidth(1f)
            setxAcc.setCircleRadius(3f)

            // line thickness and point size
            setzAcc.setLineWidth(1f)
            setzAcc.setCircleRadius(3f)

            // line thickness and point size
            setyAcc.setLineWidth(1f)
            setyAcc.setCircleRadius(3f)


            // draw points as solid circles
            setxAcc.setDrawCircleHole(false)
            setyAcc.setDrawCircleHole(false)
            setzAcc.setDrawCircleHole(false)


            // customize legend entry
            setxAcc.setFormLineWidth(1f)
            setxAcc.setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            setxAcc.setFormSize(15f)

            setyAcc.setFormLineWidth(1f)
            setyAcc.setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            setyAcc.setFormSize(15f)

            setzAcc.setFormLineWidth(1f)
            setzAcc.setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            setzAcc.setFormSize(15f)


            // text size of values
            setxAcc.setValueTextSize(9f)
            setyAcc.setValueTextSize(9f)
            setzAcc.setValueTextSize(9f)


            // draw selection line as dashed
            setxAcc.enableDashedHighlightLine(10f, 5f, 0f)
            setyAcc.enableDashedHighlightLine(10f, 5f, 0f)
            setzAcc.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            setxAcc.setDrawFilled(true)
            setxAcc.setFillFormatter({ dataSet, dataProvider -> chart!!.axisLeft.axisMinimum })

            setyAcc.setDrawFilled(true)
            setyAcc.setFillFormatter({ dataSet, dataProvider -> chart!!.axisLeft.axisMinimum })

            setzAcc.setDrawFilled(true)
            setzAcc.setFillFormatter({ dataSet, dataProvider -> chart!!.axisLeft.axisMinimum })

            // set color of filled area
            if (Utils.getSDKInt() >= 10) {
                // drawables only supported on api level 18 and above
                val drawable_RED = ContextCompat.getDrawable(this, R.drawable.fade_red)
                val drawable_CYAN = ContextCompat.getDrawable(this,R.drawable.fade_cyan)
                val drawable_MAGNETA = ContextCompat.getDrawable(this,R.drawable.fade_magneta)

                setxAcc.setFillDrawable(drawable_RED)
                setyAcc.setFillDrawable(drawable_CYAN)
                setzAcc.setFillDrawable(drawable_MAGNETA)

            } else {
                setxAcc.setFillColor(Color.BLUE)
                setyAcc.setFillColor(Color.YELLOW)
                setzAcc.setFillColor(Color.GREEN)
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(setxAcc) // add the data sets
            dataSets.add(setyAcc)
            dataSets.add(setzAcc)

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart!!.data = data
        }


    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }


    override fun saveToGallery() {
        saveToGallery(chart!!, "NextBaseChart")
    }


    internal fun actionSave (){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            saveToGallery()
        } else {
            requestStoragePermission(chart)
        }

    }




}