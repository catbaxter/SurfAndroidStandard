/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.event.lifecycle.resume;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnResumeEventResolver extends MultipleScreenEventResolver<OnResumeEvent, OnResumeDelegate, Void> {
    @Override
    public Class<OnResumeDelegate> getDelegateType() {
        return OnResumeDelegate.class;
    }

    @Override
    public Class<OnResumeEvent> getEventType() {
        return OnResumeEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected Void resolve(OnResumeDelegate delegate, OnResumeEvent event) {
        delegate.onResume();
        return null;
    }
}
