//
//  ActivityView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 21.04.2021.
//

import SwiftUI

struct ActivityView: View {
    var activity: Activity

    var body: some View {
        HStack {
            Text(activity.name)
                .font(.system(size: 18))
                .foregroundColor(.white)
            Spacer()
            Text("\(String(format: "%.2f", activity.caloriesPerMinute)) kcal/kg/min")
                .font(.system(size: 18))
                .foregroundColor(.white)
        }
    }
}
