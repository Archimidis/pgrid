/*
 * This file is part of the pgrid project.
 *
 * Copyright (c) 2012. Vourlakis Nikolas. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pgrid.process.meeting;

import com.google.inject.AbstractModule;
import pgrid.process.annotations.Scheduled;
import pgrid.process.meeting.internal.MeetingProvider;
import pgrid.process.meeting.internal.ScheduledMeetingProvider;

/**
 * @author Vourlakis Nikolas <nvourlakis@gmail.com>
 */
public class MeetingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PeerMeetingProcess.class).toProvider(MeetingProvider.class);
        bind(PeerMeetingProcess.class)
                .annotatedWith(Scheduled.class)
                .toProvider(ScheduledMeetingProvider.class);
    }
}
