package com.android.game.clash_of_the_balls;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.game.clash_of_the_balls.game.IDrawable;
import com.android.game.clash_of_the_balls.game.IMoveable;
import com.android.game.clash_of_the_balls.game.RenderHelper;
import com.android.game.clash_of_the_balls.menu.MainMenu;
import com.android.game.clash_of_the_balls.menu.MenuBackground;

/**
 * UIHandler
 * this class controls which view (menu, game) is currently active and displayed
 *
 */
public class UIHandler implements IDrawable, IMoveable, ITouchInput {
	
	private static final String LOG_TAG = "UIHandler";
	
	
	private GameSettings m_settings;
	private Context m_activity_context;
	private IMoveable m_fps_counter;
	private TextureManager m_tex_manager;
	
	private UIBase m_active_ui;
	
	private UIBase m_main_menu;
	private UIBase m_game_ui;
	
	private Font2D m_menu_item_font;
	private MenuBackground m_main_menu_background;
	
	public enum UIChange {
		NO_CHANGE,
		MAIN_MENU,
		EXIT_APPLICATION,
		
		GAME
	}
	
	public UIHandler(int screen_width, int screen_height
			, Context activity_context) {
		
		m_settings = new GameSettings();
		m_settings.m_screen_width = screen_width;
		m_settings.m_screen_height = screen_height;
		m_fps_counter = new FPSCounter();
		m_activity_context = activity_context;
		m_tex_manager = new TextureManager(m_activity_context);
		m_menu_item_font = new Font2D();
		
		
		//m_main_menu_background = new MenuBackground(
		//		m_tex_manager.get(R.raw.texture_main_menu_bg), 1.f);
		m_main_menu = new MainMenu(m_menu_item_font, m_main_menu_background
				, m_settings.m_screen_width, m_settings.m_screen_height);
		
		//TODO: init menu's , game
		
		
		m_active_ui = m_main_menu; //show main menu
	}

	@Override
	public void move(float dsec) {
		if(m_active_ui != null) {
			m_active_ui.move(dsec);

			switch(m_active_ui.UIChange()) {
			case GAME: uiChange(m_active_ui, m_game_ui);
			break;
			case MAIN_MENU: uiChange(m_active_ui, m_main_menu);
			break;
			case EXIT_APPLICATION: exitApplication();
			break;
			case NO_CHANGE: //nothing to do
			}
		}
		
		m_fps_counter.move(dsec);
	}
	
	private void uiChange(UIBase old_ui, UIBase new_ui) {
		if(old_ui != new_ui) {
			old_ui.onDeactivate();
			new_ui.onActivate();
			m_active_ui = new_ui;
		}
	}

	@Override
	public void draw(RenderHelper renderer) {
		if(m_active_ui != null) m_active_ui.draw(renderer);
	}

	@Override
	public void onTouchEvent(float x, float y, int event) {
		Log.v(LOG_TAG, "Touch event: x="+x+", y="+y+", event="+event);
		
		if(m_active_ui != null) m_active_ui.onTouchEvent(x, y, event);
		
	}
	
	private void exitApplication() {
		//we exit by starting home activity
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		m_activity_context.startActivity(intent);
	}
	
}