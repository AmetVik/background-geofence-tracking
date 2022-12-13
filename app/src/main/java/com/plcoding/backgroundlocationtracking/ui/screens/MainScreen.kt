package com.plcoding.backgroundlocationtracking.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.plcoding.backgroundlocationtracking.GeofenceService
import com.plcoding.backgroundlocationtracking.ui.theme.Teal
import java.io.Serializable

@Composable
fun MainScreen(
    context: Context
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var verticesList by remember {
            mutableStateOf(listOf<LatLng>())
        }
        AddVertex(context) { vertex ->
            verticesList = verticesList + vertex
        }
        Vertices(verticesList)
        ToggleService(context, verticesList)
    }
}

@Composable
fun ToggleService(
    context: Context,
    polygon: List<LatLng>
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            if (polygon.size > 3) {
                Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show()
                Intent(context, GeofenceService::class.java).apply {
                    action = GeofenceService.ACTION_START
                    putExtra("VERTICES_LIST", polygon as Serializable)
                    context.startService(this)
                }
            } else {
                Toast.makeText(context, "Please enter at least 3 vertices", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {
            Text(text = "Start Tracking")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            Toast.makeText(context, "Service Stopped", Toast.LENGTH_SHORT).show()

            Intent(context, GeofenceService::class.java).apply {
                action = GeofenceService.ACTION_STOP
                context.startService(this)
            }
        }) {
            Text(text = "Stop Tracking")
        }
    }
    Button(onClick = {
        Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show()
        Intent(context, GeofenceService::class.java).apply {
            action = GeofenceService.ACTION_START
            putExtra("VERTICES_LIST", polygon as Serializable)
            context.startService(this)
        }
    }) {
        Text(text = "Start Tracking (Hard Coded)")
    }
}


@Composable
fun AddVertex(
    context: Context,
    onVertexAdded: (LatLng) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var vertexLat by remember { mutableStateOf("") }
        var vertexLng by remember { mutableStateOf("") }

        Text(
            text = "Geo-Fencing",
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(top = 12.dp)
        )
        //Latitude
        TextField(
            value = vertexLat,
            onValueChange = { vertexLat = it },
            label = { Text(text = "Enter Latitude of vertex..", color = Color.White) },
            textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Teal,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        //Longitude
        TextField(
            value = vertexLng,
            onValueChange = { vertexLng = it },
            label = { Text("Enter Longitude of vertex..", color = Color.White) },
            textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Teal,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (vertexLat !== "" && vertexLng !== "") {
                    val vertex = LatLng(vertexLat.toDouble(), vertexLng.toDouble())
                    onVertexAdded(vertex)
                    vertexLat = ""
                    vertexLng = ""
                } else {
                    Toast.makeText(context, "Please enter the Co-Ords", Toast.LENGTH_SHORT).show()
                }

            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = Teal,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Add a vertex..",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )
        }

    }
}

@Composable
fun Vertices(
    vertices: List<LatLng>,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(250.dp)
    ) {
        items(vertices.size) { index ->
            Card(
                elevation = 10.dp,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Vertex ${index + 1} ",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Lat - ${vertices[index].latitude}",
                            color = Color.Red,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Italic
                        )
                        Text(
                            text = "Lng - ${vertices[index].longitude}",
                            color = Color.Red,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

            }
        }
    }
}

