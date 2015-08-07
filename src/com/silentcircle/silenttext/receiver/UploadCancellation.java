/*
Copyright (C) 2013-2015, Silent Circle, LLC. All rights reserved.

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
package com.silentcircle.silenttext.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.silentcircle.silenttext.Extra;
import com.silentcircle.silenttext.R;
import com.silentcircle.silenttext.application.SilentTextApplication;
import com.silentcircle.silenttext.model.event.Event;
import com.silentcircle.silenttext.repository.ConversationRepository;
import com.silentcircle.silenttext.repository.EventRepository;

public class UploadCancellation extends BroadcastReceiver {

	private static SilentTextApplication getApplication( Context context ) {
		return SilentTextApplication.from( context );
	}

	private static ConversationRepository getConversations( Context context ) {
		return getApplication( context ).getConversations();
	}

	private static EventRepository getHistory( Context context, String partner ) {
		ConversationRepository conversations = getConversations( context );
		if( conversations != null && conversations.exists() ) {
			return conversations.historyOf( conversations.findByPartner( partner ) );
		}
		return null;
	}

	private static void removeEvent( Context context, String partner, String eventID ) {
		EventRepository events = getHistory( context, partner );
		if( events != null && events.exists() ) {
			Event event = events.findById( eventID );
			events.remove( event );
			SilentTextApplication.from( context ).removeAttachments( event );
		}
	}

	@Override
	public void onReceive( Context context, Intent intent ) {
		removeEvent( context, Extra.PARTNER.from( intent ), Extra.ID.from( intent ) );
		String reason = Extra.TEXT.from( intent );
		Toast.makeText( context, reason == null ? context.getString( R.string.cancelled ) : reason, Toast.LENGTH_SHORT ).show();
	}

}
