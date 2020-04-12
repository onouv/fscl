import sut from './EntityManager'
import { cloneDeep } from 'lodash'

import Entity from '../domain/Entity'

describe("given root-level entities =001, =002, =003 PRISTINE", () => {

   const project = "P001"
   const e1 = new Entity({
      initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=001"}})
   const e2 = new Entity({
      initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=002"}})
   const e3 = new Entity({
      initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=003"}})

   const reference = [e1, e2, e3]

   let roots = []

   describe("given =004 as new id", () => {

      let newIds = []
      const e4Id = { project: project, entity: "=004"}

      beforeEach(() => {
         newIds = []
         newIds.push(e4Id)
      })

      describe("when called to clone with inserted entities", () => {

         let newRoots = []
         beforeEach(() => {
            roots = []
            roots.push(e1.value, e2.value, e3.value)
            newRoots = sut.clone.withNewEntitiesFromCodes(roots, newIds)
         })

         it("then should append behind =003", () => {
            expect(newRoots.length).toEqual(4)
            expect(newRoots[0].self.entity).toEqual("=001")
            expect(newRoots[1].self.entity).toEqual("=002")
            expect(newRoots[2].self.entity).toEqual("=003")
            expect(newRoots[3].self.entity).toEqual("=004")
         })
      })

      describe("given childs =002.001 and =002.002", () => {

         const e21 =  new Entity({self: { project: project, entity: "=002.001"}})
         const e22 =  new Entity({self: { project: project, entity: "=002.002"}})

         describe("when called to clone with inserted entities", () => {

            let newRoots = []
            beforeEach(() => {
               roots = []
               roots.push(e1.value, e2.value, e21.value, e22.value, e3.value)
               newRoots = sut.clone.withNewEntitiesFromCodes(roots, newIds)
            })

            it("then should append behind =003", () => {
                  expect(newRoots.length).toEqual(6)
                  expect(newRoots[0].self.entity).toEqual("=001")
                  expect(newRoots[1].self.entity).toEqual("=002")
                  expect(newRoots[2].self.entity).toEqual("=002.001")
                  expect(newRoots[3].self.entity).toEqual("=002.002")
                  expect(newRoots[4].self.entity).toEqual("=003")
                  expect(newRoots[5].self.entity).toEqual("=004")
            })
         })
      })
   })

   describe("given =002.001 as new id", () => {

      const e21Id = { project: project, entity: "=002.001"}
      const newIds = [ e21Id ]

      describe("when called to clone with inserted entities", () => {

         let newRoots = []
         beforeEach(() => {
            roots = []
            roots.push(e1.value, e2.value, e3.value)
            newRoots = sut.clone.withNewEntitiesFromCodes(roots, newIds)
         })

         it("then should insert behind =002", () => {
            expect(newRoots.length).toEqual(4)
            expect(newRoots[0].self.entity).toEqual("=001")
            expect(newRoots[1].self.entity).toEqual("=002")
            expect(newRoots[2].self.entity).toEqual("=002.001")
            expect(newRoots[3].self.entity).toEqual("=003")
         })
      })
   })

   describe("given =002.002 as new id", () => {

      const e22 = { project: project, entity: "=002.002"}
      const newIds = [ e22 ]

      describe("when called to clone with inserted entities", () => {

         let newRoots = []
         beforeEach(() => {
            roots = []
            roots.push(e1.value, e2.value, e3.value)
            newRoots = sut.clone.withNewEntitiesFromCodes(roots, newIds)
         })

         it("then should insert behind =002", () => {
            expect(newRoots.length).toEqual(4)
            expect(newRoots[0].self.entity).toEqual("=001")
            expect(newRoots[1].self.entity).toEqual("=002")
            expect(newRoots[2].self.entity).toEqual("=002.002")
            expect(newRoots[3].self.entity).toEqual("=003")
         })
      })
   })

   describe("given child =002.003", () => {

      const e23 =  new Entity({self: { project: project, entity: "=002.003"}})

      describe("given childs =002.001 and =002.002 as new Ids", () => {

         const newIds = [
            { project: project, entity: "=002.001" },
            { project: project, entity: "=002.002" }
         ]

         describe("when called to clone with inserted entities", () => {

            let newRoots = []
            beforeAll(() => {
               roots = []
               roots.push(e1.value, e2.value, e23.value, e3.value)

               newRoots = sut.clone.withNewEntitiesFromCodes(roots, newIds)
            })

            it("should insert between =002 and =002.003", () => {
               expect(newRoots.length).toEqual(6)
               expect(newRoots[0].self.entity).toEqual("=001")
               expect(newRoots[1].self.entity).toEqual("=002")
               expect(newRoots[2].self.entity).toEqual("=002.001")
               expect(newRoots[3].self.entity).toEqual("=002.002")
               expect(newRoots[4].self.entity).toEqual("=002.003")
               expect(newRoots[5].self.entity).toEqual("=003")
            })
         })
      })
   })

   describe("given =001 and =003 PRISTINE just SAVEd with new content", () => {

      const desc = "asdkljkljaspoui 4234"
      const name = "ölölasdpoüpo asdas"

      const e1Saved = new Entity({
         initialState: Entity.PRISTINE,
         self: { project: project, entity: "=001"},
         content: { name: name, description: desc },
         unfolded: false,
         foldingEnabled: false
      })

      const e3Saved = new Entity({
         initialState: Entity.PRISTINE,
         self: { project: project, entity: "=003"},
         content: { name: name, description: desc },
         unfolded: false,
         foldingEnabled: false
      })

      describe("when called SAVE with new content", () => {

         let clones = []
         let savedEntities = []

         beforeEach(() => {
            roots = []
            roots.push(e1.value, e2.value, e3.value)
            savedEntities.push(e1Saved.value)
            savedEntities.push(e3Saved.value)
         })

         it("then should update entity =001 ", () => {

            clones = sut.clone.withSavedEntities(roots, savedEntities)

            expect(clones).toBeInstanceOf(Array)
            expect(clones.length).toBe(3)

            const clone1 = clones [0]
            checkCloneAgainstEntity(clone1, e1)

            const clone3 = clones [2]
            checkCloneAgainstEntity(clone3, e3)

            function checkCloneAgainstEntity(clone, entity) {
               expect(clone.self).toEqual(entity.self.value)
               expect(clone.parent).toEqual(entity.parent)
               expect(clone.content.name).toEqual(name)
               expect(clone.content.description).toEqual(desc)
               expect(clone.pristine).toEqual(false)
               expect(clone.marked).toEqual(false)
               expect(clone.changed).toEqual(false)
               expect(clone.unfolded).toEqual(entity.unfolded)
               expect(clone.foldingEnabled).toEqual(entity.foldingEnabled)
            }
         })
      })
   })
})

describe("given entities =001, =002, =003 UNDEFINED", () => {

   const project = "P001"
   const e1 = new Entity({
      //initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=001"}})
   const e2 = new Entity({
      //initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=002"}})
   const e3 = new Entity({
      //initialState: Entity.State.PRISTINE,
      self: { project: project, entity: "=003"}})

   const reference = [e1.value, e2.value, e3.value]

   describe("when called for LOAD", () => {

      let loadedEntitiesRaw = []
      let loadedEntities = []

      beforeEach(() => {
         loadedEntitiesRaw = []
         loadedEntitiesRaw.push(e1.value, e2.value, e3.value)
         loadedEntities = sut.init.rawEntities(loadedEntitiesRaw)
      })

      it("then should initialize all three in Entity.State.SAVED", () => {

         loadedEntities.forEach(e=>{
            const checked = new Entity({...e})
            expect(checked.state).toEqual(Entity.State.SAVED)
         })
      })

      it("then should initialize all three with given values",() => {

         expect(reference.length).toEqual(loadedEntities.length)

         for(let i=0; i < loadedEntities.length; i++) {
            expect(loadedEntities[i].self).toEqual(reference[i].self)
            expect(loadedEntities[i].content).toEqual(reference[i].content)
            expect(loadedEntities[i].unfolded).toEqual(reference[i].unfolded)
            expect(loadedEntities[i].foldingEnabled).toEqual(false)
         }
      })
   })
})

describe("given entities =001, =002, =003 SAVED", () => {

   const project = "P001"
   const e1 = new Entity({
      initialState: Entity.State.SAVED,
      self: { project: project, entity: "=001"}})
   const e2 = new Entity({
      initialState: Entity.State.SAVED,
      self: { project: project, entity: "=002"}})
   const e3 = new Entity({
      initialState: Entity.State.SAVED,
      self: { project: project, entity: "=003"}})

   const reference = [e1.value, e2.value, e3.value]

   describe("given content change for =002", () => {

      const contentChange = {
         code: { project: "P001", entity: "=002" },
         name: "Willi",
         description: "So ein Willi!"
      }

      let containsChanged = []
      let allUnchanged = []

      describe("when called for CHANGE", () => {

         beforeAll(() => {
            containsChanged = []
            allUnchanged = cloneDeep(reference)

            containsChanged= sut.clone.withChangedEntity(
               allUnchanged,
               contentChange)

         })

         it("then should flag =002 and only =002 as changed", () => {

            expect(containsChanged.length).toEqual(3)
            expect(containsChanged[0].changed).toBe(false)
            expect(containsChanged[1].changed).toBe(true)
            expect(containsChanged[2].changed).toBe(false)
         })

         it("then should update the content of and only of =002", () => {
            const content = { name: contentChange.name, description: contentChange.description }
            expect(containsChanged.length).toEqual(3)
            expect(containsChanged[0].content).toEqual(reference[0].content)
            expect(containsChanged[1].content).toEqual(content)
            expect(containsChanged[2].content).toEqual(reference[2].content)
         })

         it("then should leave the marked flag untouched", () => {
            expect(containsChanged.length).toEqual(3)
            expect(containsChanged[0].marked).toBe(allUnchanged[0].marked)
            expect(containsChanged[1].marked).toBe(true)
            expect(containsChanged[2].marked).toBe(allUnchanged[2].marked)
         })

      })
   })
})

describe("given =001 SAVED, =002 and =003 CHANGED", () => {

   const project = "P001"
   const content = { name: "Diddi", description: "wahdiddie"}
   const e1SAVED = new Entity({
      initialState: Entity.State.SAVED,
      self: { project: project, entity: "=001"},
      content: content,
      unfolded: false,
      foldingEnabled: true
   })
   const e2CHANGED = new Entity({
      initialState: Entity.State.CHANGED,
      self: { project: project, entity: "=002"},
      content: content,
      unfolded: false,
      foldingEnabled: true
   })
   const e3CHANGED = new Entity({
      initialState: Entity.State.CHANGED,
      self: { project: project, entity: "=003"},
      content: content,
      unfolded: false,
      foldingEnabled: false
   })


   describe("given =002 and =003 returned from server w/o error", () => {

      const e2SAVED = new Entity({
         initialState: Entity.State.SAVED,
         self: { project: project, entity: "=002"},
         content: content
      })
      const e3SAVED = new Entity({
         initialState: Entity.State.SAVED,
         self: { project: project, entity: "=003"},
         content: content,
      })

      describe("whan called for SAVE", () => {

         const fromState = [e1SAVED.value, e2CHANGED.value, e3CHANGED.value]
         let forState = []

         beforeAll(() => {
            const saved = [e2CHANGED.value, e3CHANGED.value]
            forState = sut.clone.withSavedEntities(fromState, saved)
         })

         it("then =002 and =003 should be in SAVED", () => {
            expect(forState.length).toBe(3)
            expect(forState[1].pristine).toBe(false)
            expect(forState[1].changed).toBe(false)
            expect(forState[2].pristine).toBe(false)
            expect(forState[2].changed).toBe(false)

         })

         it("then =002 and =003 should have same identity as before, respectively", () => {
            expect(forState.length).toBe(3)
            expect(forState[1].self).toEqual(fromState[1].self)
            expect(forState[2].self).toEqual(fromState[2].self)
         })

         it("then =002 and =003 should have same content as before, respectively", () => {
            expect(forState.length).toBe(3)
            expect(forState[1].content).toEqual(fromState[1].content)
            expect(forState[2].content).toEqual(fromState[2].content)
         })

         it("then =002 and =003 should have same folding status as before, respectively", () => {
            expect(forState.length).toBe(3)
            expect(forState[1].unfolded).toBe(fromState[1].unfolded)
            expect(forState[2].foldingEnabled).toBe(fromState[2].foldingEnabled)
         })
      })
   })
})
