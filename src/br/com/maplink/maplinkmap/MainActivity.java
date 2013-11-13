package br.com.maplink.maplinkmap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import br.com.maplink.core.Pin;
import br.com.maplink.core.Point;
import br.com.maplink.core.exception.MapLinkAPIException;
import br.com.maplink.core.listener.MapAlreadyLoadedListener;
import br.com.maplink.map.MapTemplate;
import br.com.maplink.map.listener.MapLongClickListener;
import br.com.maplink.map.listener.MapMoveEndListener;
import br.com.maplink.map.listener.MapTiltEndListener;
import br.com.maplink.map.listener.MapTouchListener;
import br.com.maplink.map.listener.MapZoomEndListener;

public class MainActivity extends Activity {
	private static final String TAG = "MapLink";
	// String token = "Your access token here";
	String token = "c13iyCvmcC9mzwkLd0LCbmYC5mUF5m2jNGNtNGt6NmK6NJK=";
	MapTemplate map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		try {
			map = (MapTemplate) findViewById(R.id.map);
			Point point = new Point(-23.592310, -46.689228);
			map.startMapOn(token, point);

			/**
			 * Habilitando o zoom com double touch
			 */
			map.addDoubleTouchZoomControl();

			
			//-- Adicionando controle físicos de zoom ao mapa
			
			// Criando controle visual de zoo
			LinearLayout zoomControls = (LinearLayout) findViewById(R.id_map.zoomview);
			
			// Adicionando controle visual no mapa
			zoomControls.addView(map.getZoomControls());

			
			//-- Adicionando listeners ao mapa
			
			// Listener executado ao final de um movimento no mapa
			map.addMoveEndListener(new MapMoveEndListener() {

				public void onMoveEnd(MapTemplate mapTemplate, Point point) {
					Pin pin = new Pin(R.drawable.pin, point);
					mapTemplate.addPin(pin);
				}
			});

			// Listener executado ao final do zoom no mapa
			map.addZoomEndListener(new MapZoomEndListener() {

				public void onZoomEnd(MapTemplate mapTemplate, Point point, int zoomLevel) {
					mapTemplate.addPin(R.drawable.pin, point);
				}
			});

			// Listener executado ao toque longo no mapa
			map.addLongClickListener(new MapLongClickListener() {

				public void onLongClick(MapTemplate mapTemplate, Point point) {
					mapTemplate.addPin(R.drawable.pin, point);

				}
			});

			// Listener executado toda vez que o mapa termina de ser carregado
			map.addLoadedListener(new MapAlreadyLoadedListener() {

				@Override
				public void onAlreadyLoaded() {
					Log.d(TAG, "Finished load tiles");
				}
			});
			
			// Listener executado na inclinação do mapa
			map.addTiltEndListener(new MapTiltEndListener() {

				public void onTiltEnd(MapTemplate mapTemplate, float degree) {
					Toast.makeText(MainActivity.this, "Tilt event", Toast.LENGTH_LONG).show();
				}
			});

			// Listener executado ao toque simples no mapa
			map.addTouchListener(new MapTouchListener() {

				public void onTouch(MapTemplate mapTemplate, Point point) {
					Toast.makeText(MainActivity.this, "Touch event", Toast.LENGTH_LONG).show();
					mapTemplate.addPin(R.drawable.pin, point);
				}
			});

		} catch (MapLinkAPIException e) {
			e.printStackTrace();
		}
	}

}
