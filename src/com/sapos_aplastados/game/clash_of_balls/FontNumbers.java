/*
 * Copyright (C) 2012-2013 Hans Hardmeier <hanshardmeier@gmail.com>
 * Copyright (C) 2012-2013 Andrin Jenal
 * Copyright (C) 2012-2013 Beat Küng <beat-kueng@gmx.net>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */
package com.sapos_aplastados.game.clash_of_balls;

import com.sapos_aplastados.game.clash_of_balls.Font2D.Font2DSettings;
import com.sapos_aplastados.game.clash_of_balls.game.RenderHelper;
import com.sapos_aplastados.game.clash_of_balls.game.Vector;

/**
 * a class for drawing numbers. it caches the used textures for faster reuse
 * use it for non-negative integer numbers and not too large 
 * (a few hundred or thousand).
 * useful for countdowns
 *
 */
public class FontNumbers {

	private static final float font_size = 0.8f;
	private Font2D m_fonts[] = null;
	private final Vector m_texture_size;
	
	private TextureManager m_texture_manager;
	private Font2DSettings m_font_settings;
	
	public FontNumbers(TextureManager tex_manager, Font2DSettings font_settings
			, Vector texture_size) {
		m_texture_size = new Vector(texture_size);
		m_texture_manager = tex_manager;
		m_font_settings = font_settings;
	}
	
	//font initialization
	public Font2D getFont(int number) {
		if(number<0) return null;
		
		if(m_fonts == null) {
			m_fonts = new Font2D[number+1];
		} else if(m_fonts.length <= number) {
			//resize
			Font2D tmp[]=new Font2D[Math.max(number+1, m_fonts.length*2)];
			for(int i=0; i<m_fonts.length; ++i) tmp[i] = m_fonts[i];
			m_fonts = tmp;
		}
		if(m_fonts[number] == null) {
			m_fonts[number] = new Font2D(m_texture_manager, m_texture_size
					, m_font_settings, (int)(m_texture_size.y*font_size));
			m_fonts[number].setString(""+number);
		}
		return m_fonts[number];
	}
	
	public void draw(RenderHelper renderer, int number, float pos_x, float pos_y
			, float output_height) {
		
		Font2D font = getFont(number);
		if(font==null) return;
		
		float output_width = output_height * m_texture_size.x/m_texture_size.y;
		
		renderer.pushModelMat();
		renderer.modelMatTranslate(pos_x, pos_y, 0.f);
		renderer.modelMatScale(output_width, output_height, 0.f);
		font.draw(renderer);
		renderer.popModelMat();
	}
}
