/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.realityandapp.paopao_customer.views.im;

import android.app.Activity;
import com.easemob.chat.EMChatManager;

public class BaseActivity extends Activity {
	@Override
	protected void onResume() {
		super.onResume();
		//onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
	}
}