//
//  CRUDView.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import SwiftUI
import SwiftUIRefresh

struct CRUDView<T: Codable, Content>: View where Content : View {
        
    @ObservedObject var viewModel: ViewModelRead<T>
    @State var isShowing = false
    
    var reloadAction: () -> ()
    var content: Content
    
    init (viewModel: ViewModelRead<T>, reload: @escaping () -> (), @ViewBuilder content: () -> Content) {
        self.viewModel = viewModel
        self.reloadAction = reload
        self.content = content()
    }
    
    var body: some View {
        ZStack {
            if !viewModel.firstLoaded {
                ProgressView()
                    .alert(item: $viewModel.errorMessage) { errorMessage in
                        Alert(
                            title: Text("Error"),
                            message: Text(errorMessage.localizedDescription),
                            primaryButton: Alert.Button.default(
                                Text("Reload"),
                                action: reload
                            ),
                            secondaryButton: Alert.Button.cancel(
                                Text("Cancel"),
                                action: { viewModel.firstLoaded = true }
                            )
                        )
                    }
            }
            else if viewModel.entities.isEmpty {
                Text("No content found")
                    .padding([.leading, .trailing])
            }
            else {
                content
                    .pullToRefresh(isShowing: $isShowing) {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: reload)
                    }
            }
        }
        .onAppear(perform: reload)
    }
    
}

extension CRUDView {
    func reload () {
        reloadAction()
        isShowing = false
    }
}

struct CRUDView_Previews: PreviewProvider {
    static var previews: some View {
        AllergensView()
    }
}
