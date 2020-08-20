import Entity from './Entity'

describe("given entity =001", () => {
   const e1CodeRaw = { project: "P001", entity: "=001"}
   const e1ContentRaw = { name: "aklasdk", description: "Glumfiddyi pack"}
   const e1Raw = {
      self: e1CodeRaw,
      content: e1ContentRaw,
      parent: null,
      children: [],
      //unfolded: false,
      //foldingEnabled: false,
      error: null
   }

   describe("when created in PRISTINE", () => {
      let e1
      beforeEach(() => {
         e1 = new Entity({initialState: Entity.State.PRISTINE, ...e1Raw})
      })

      it("then should be PRISTINE", ()=>{
         expect(e1.state).toBe(Entity.State.PRISTINE)
      })

      it("then should be marked and changed", () => {
         expect(e1.marked).toBe(true)
         expect(e1.changed).toBe(true)
      })

      it("then should be folded and not be allowed to fold/unfold", () => {
         expect(e1.foldingEnabled).toBe(false)
         expect(e1.unfolded).toBe(false)
      })
   })

   describe("when created raw w/o children", () => {

      let e1
      beforeEach(() => {
         e1 = new Entity({...e1Raw})
      })

      it("then should be in state SAVED", () => {
         expect(e1.state).toBe(Entity.State.SAVED)
      })

      describe("when marked", () => {

         beforeEach(() => {
            e1 = new Entity({...e1Raw})
            e1.marked = true
         })

         it("then should be in state SAVED", () => {
            expect(e1.state).toBe(Entity.State.SAVED)
         })
      })


      describe("when unfolded", () => {

         beforeEach(() => {
            e1 = new Entity({...e1Raw})
            e1.unfolded = true
         })

         it("then should be in state SAVED", () => {
            expect(e1.state).toBe(Entity.State.SAVED)
         })
      })

      describe("when allowed to unfold and fold", () => {

         beforeEach(() => {
            e1 = new Entity({...e1Raw})
            e1.foldingEnabled = true
         })

         it("then should be in state UNDEFINE", () => {
            expect(e1.state).toBe(Entity.State.UNDEFINED)
         })
      })

      describe("when flagged as changed ", () => {

         beforeEach(() => {
            e1 = new Entity({...e1Raw})
            e1.changed = true
         })

         it("then should be in state CHANGED", () => {
            expect(e1.state).toBe(Entity.State.CHANGED)
         })
      })
   })
})
