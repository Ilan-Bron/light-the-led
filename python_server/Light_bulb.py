'''
@author: ilang
'''


"""
This class represents the pi3 lightbulb and all its actions
"""

import time
import gpiozero  

#the dictionary to save different actions the light bulb can do
action_dict = {
    
    
    
            }



class light_bulb:
    
    
    def __init__(self , pin_num):
        
        self.pin_num = pin_num
        self.led = gpiozero.LED(pin_num)
    
    
    
    def flicker(self, time_of_action , down_time):
        
        down_time = int(down_time)
        
        for i in range(time_of_action/down_time):
            self.led.toggle()
            time.sleep(down_time)
        
    
    def turn_on(self , time_of_action = -1):
        self.led.on()
        if(time_of_action != -1):
            time.sleep(time_of_action)
        
        
    def turn_off(self , time_of_action = -1):
        self.led.off()
        if(time_of_action != -1):
            time.sleep(time_of_action)

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
