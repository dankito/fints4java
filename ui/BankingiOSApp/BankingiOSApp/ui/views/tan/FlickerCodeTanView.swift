import SwiftUI
import BankingUiSwift


struct FlickerCodeTanView: View {
    
    private static let SpaceBetweenStripesStepSize: CGFloat = 0.5
    private static let StripesWidthStepSize: CGFloat = 2.35 * Self.SpaceBetweenStripesStepSize
    
    
    private let MinScaleFactor: CGFloat = 10
    private let MaxScaleFactor: CGFloat
    
    
    private var tanChallenge: BankingUiSwift.FlickerCodeTanChallenge
    
    @State private var showBit1: Bool = true
    @State private var showBit2: Bool = true
    @State private var showBit3: Bool = true
    @State private var showBit4: Bool = true
    @State private var showBit5: Bool = true
    
    @State private var animator: FlickerCodeAnimator = FlickerCodeAnimator() // real curious bug: if @State is omitted setting frequency doesn't work (why?)
    
    
    private var tanMethodSettings: TanMethodSettings? = nil
    
    @State private var frequency = CGFloat(FlickerCodeAnimator.DefaultFrequency)
    
    private var frequencyBinding: Binding<CGFloat> {
        Binding<CGFloat>(
            get: { self.frequency },
            set: {
                if (self.frequency != $0) {
                    let newFrequency = $0

                    self.animator.setFrequency(frequency: Int(newFrequency))
                    
                    DispatchQueue.main.async {
                        self.frequency = newFrequency
                    }
                }
        })
    }
    
    
    @State private var stripeWidth: CGFloat = 24
    @State private var spaceBetweenStripes: CGFloat = 10
    @State private var spaceBetweenTanGeneratorPositionMarker: CGFloat = 4 * 40 + 4 * 15 - TanGeneratorPositionMarker.Width
    
    @State private var scaleFactor: CGFloat = 20.0
    
    private var scaleFactorBinding: Binding<CGFloat> {
        Binding<CGFloat>(
            get: { self.scaleFactor },
            set: {
                if (self.scaleFactor != $0) {
                    let newFlickerScaleFactor = $0
                    
                    DispatchQueue.main.async {
                        self.scaleFactor = newFlickerScaleFactor
                        
                        self.calculateStripeWidth()
                    }
                }
        })
    }
    
    
    @State private var isInitialized = false
    
    
    @Inject private var presenter: BankingPresenterSwift
    
    
    init(_ tanChallenge: BankingUiSwift.FlickerCodeTanChallenge) {
        self.tanChallenge = tanChallenge
        
        let oneStepDiff = 5 * Self.StripesWidthStepSize + 4 * Self.SpaceBetweenStripesStepSize
        MaxScaleFactor = CGFloat(Int(UIScreen.main.bounds.width / oneStepDiff))
        
        self.tanMethodSettings = presenter.appSettings.flickerCodeSettings
        
        if let settings = tanMethodSettings {
            self._scaleFactor = State(initialValue: CGFloat(settings.width))
            self._frequency = State(initialValue: CGFloat(settings.frequency))
        }

        animator.setFrequency(frequency: Int(frequency))
    }
    
    
    var body: some View {
        Section {
            HStack {
                Text("Tan Generator Frequency")
                
                Spacer()
                
                Image(systemName: "tortoise")
                
                Slider(value: frequencyBinding, in: CGFloat(FlickerCodeAnimator.MinFrequency)...CGFloat(FlickerCodeAnimator.MaxFrequency), step: 1)
                
                Image(systemName: "hare")
            }
            
            ScaleImageView(scaleFactorBinding, imageMinWidth: MinScaleFactor, imageMaxWidth: MaxScaleFactor, step: 1)
            
            VStack {
                HStack {
                    Spacer()
                    
                    TanGeneratorPositionMarker()
                    
                    Spacer()
                    .frame(width: spaceBetweenTanGeneratorPositionMarker)
                    
                    TanGeneratorPositionMarker()
                    
                    Spacer()
                }
                .padding(.bottom, -4)
                
                HStack {
                    Spacer()

                    HStack {
                        FlickerCodeStripe($showBit1, $stripeWidth)
                        
                        Spacer()
                        .frame(width: spaceBetweenStripes)
                        
                        FlickerCodeStripe($showBit2, $stripeWidth)
                        
                        Spacer()
                        .frame(width: spaceBetweenStripes)
                        
                        FlickerCodeStripe($showBit3, $stripeWidth)
                        
                        Spacer()
                        .frame(width: spaceBetweenStripes)
                        
                        FlickerCodeStripe($showBit4, $stripeWidth)
                        
                        Spacer()
                        .frame(width: spaceBetweenStripes)
                        
                        FlickerCodeStripe($showBit5, $stripeWidth)
                    }
                    
                    Spacer()
                }
                .padding(.bottom, 12)
            }
            .background(Color.black)
            .removeListInsets()
        }
        // what a hack to be able to call animator.animate() (otherwise compiler would throw 'use of immutable self in closure' error)
        .executeMutatingMethod {
            if isInitialized == false {
                isInitialized = true
                
                self.calculateStripeWidth()
                
                self.animator.animate(self.tanChallenge.flickerCode.parsedDataSet, self.showStep)
            }
        }
        .onDisappear {
            self.saveChanges()
        }
    }
    
    
    private func showStep(_ step: Step) {
        self.showBit1 = step.bit1.isHigh
        self.showBit2 = step.bit2.isHigh
        self.showBit3 = step.bit3.isHigh
        self.showBit4 = step.bit4.isHigh
        self.showBit5 = step.bit5.isHigh
    }
    
    
    private func calculateStripeWidth() {
        stripeWidth = scaleFactor * Self.StripesWidthStepSize
        spaceBetweenStripes = scaleFactor * Self.SpaceBetweenStripesStepSize
        
        spaceBetweenTanGeneratorPositionMarker = 4 * stripeWidth + 4 * spaceBetweenStripes - TanGeneratorPositionMarker.Width
    }
    
    
    private func saveChanges() {
        let scaleFactorInt = Int32(scaleFactor)
        let frequencyInt = Int32(frequency)
        
        if scaleFactorInt != tanMethodSettings?.width || frequencyInt != tanMethodSettings?.frequency {
            let settings = tanMethodSettings ?? TanMethodSettings(width: 0, height: 0, space: 0, frequency: 0)
            
            settings.width = scaleFactorInt
            settings.frequency = frequencyInt
            
            presenter.updateTanMethodSettings(tanMethod: tanChallenge.tanMethod, settings: settings)
        }
    }
    
}


struct FlickerCodeTanView_Previews: PreviewProvider {

    static var previews: some View {
        FlickerCodeTanView(previewFlickerCodeTanChallenge)
    }

}
