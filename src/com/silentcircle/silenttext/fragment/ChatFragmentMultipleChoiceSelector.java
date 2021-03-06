/*
Copyright (C) 2014-2015, Silent Circle, LLC. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Any redistribution, use, or modification is done solely for personal
      benefit and not for any commercial purpose or for monetary gain
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name Silent Circle nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SILENT CIRCLE, LLC BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.silentcircle.silenttext.fragment;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.silentcircle.silenttext.R;
import com.silentcircle.silenttext.fragment.ChatFragment.Callback;
import com.silentcircle.silenttext.listener.MultipleChoiceSelector;
import com.silentcircle.silenttext.model.event.Event;
import com.silentcircle.silenttext.view.HasChoiceMode;

public class ChatFragmentMultipleChoiceSelector extends MultipleChoiceSelector<Event> {

	private final ChatFragment fragment;

	public ChatFragmentMultipleChoiceSelector( ChatFragment fragment, HasChoiceMode choices, int menuResourceID, String titleFormat ) {
		super( choices, menuResourceID, fragment, titleFormat );
		this.fragment = fragment;
	}

	@Override
	public boolean onCreateActionMode( ActionMode mode, Menu menu ) {
		boolean result = super.onCreateActionMode( mode, menu );
		Callback callback = fragment.getCallback();
		if( callback != null ) {
			callback.onActionModeCreated();
		}
		return result;
	}

	@Override
	public void onDestroyActionMode( ActionMode mode ) {
		super.onDestroyActionMode( mode );
		Callback callback = fragment.getCallback();
		if( callback != null ) {
			callback.onActionModeDestroyed();
		}
	}

	@Override
	public void onItemCheckedStateChanged( ActionMode mode, int position, long itemId, boolean checked ) {
		super.onItemCheckedStateChanged( mode, position, itemId, checked );
		mode.invalidate();
	}

	@Override
	public boolean onPrepareActionMode( ActionMode mode, Menu menu ) {
		MenuItem item = menu.findItem( R.id.copy );
		boolean before = item.isVisible();
		item.setVisible( !fragment.hasMultipleCheckedItems() );
		boolean after = item.isVisible();
		return super.onPrepareActionMode( mode, menu ) || before != after;
	}

}
