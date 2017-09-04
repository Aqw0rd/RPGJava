
class FSM:
    def __init__(self,initialstate):
        self.stack = [initialstate]

    def pushState(self, state):
        self.stack.append(state)

    def popState(self):
        self.stack.pop()

    def update(self):
        curstate = self.getCurrentState()
        curstate()

    def getCurrentState(self):
        return self.stack[len(self.stack)-1]