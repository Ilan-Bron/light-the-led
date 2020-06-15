'''
@author: ilang
'''

"""
This class will be the main part of the server that will handle the different connections
"""

#the messages thet will come will contain a set of instruction
#every instruction will be represented by an action the operand and the time seperated by zeros

import  threading
import socket 
import Light_bulb
import Save_state
import os

IP = '0.0.0.0'
PORT =  12345


class handle_connection(threading.Thread):

    def run(self):
        
        self.user_name = self.recieve_message()
        
        self.user_str = Save_state.check_user(self.user_name)
                                                                        #check the user connected
    
        print("connection recieved from "  + self.client_address.__str__())
        print("username: " , self.user_name)
        print("action string " , self.user_str)
        
        self.send_message(self.user_str)
        
        while self.keep_run:
            
            self.reset_leds()
                    
            message = self.recieve_message()
            print("new message recieved:" , message)
            
            self.send_message("1")
            
            if(len(message) > 0):
            
                if(message == "r"):    #r means run the message
                    
                    print("running the message")                       
                    self.handle_message(self.user_str)
                    
                    
                elif(message[0] == "n"):         #n is new message
                    self.change_message(message[1::])
                    
                    print("message was changed to " , message[1::])
                    
                elif(message[0] == "1"):
                    print("running message once")
                    
                    self.handle_message(message[1::])
                    
                    
                    
            else:
                self.close_connection()
                
        
        print("ending session with" , self.user_name)
        
        
    
    def __init__(self , connection , client_address):
        super(handle_connection, self).__init__()
        self.connection = connection
        self.client_address = client_address
        self.keep_run = True
        self.user_str = ""
        self.user_name = ""
        
    
    
    def recieve_message(self):
        message = self.connection.recv(1024)
        return message.decode()
    
    def send_message(self , message):
        print("sending message" , message)
        self.connection.send(message.encode())
        
    
    def close_connection(self):
        self.keep_run = False
        self.connection.close()
        
    
    
    
    def change_message(self , message):
        self.user_str = message 
        
        Save_state.save_json(self.user_name, message)
    
    
      
    
    def handle_message(self , message):
                
        actions_list = message.split("_") #divide all the actions into different list elements
        print("action list:" , actions_list)
        
        
        for action in actions_list:
            
            arg_list = action.split(".")
            
            action_type = arg_list[0]
            action_time = int(arg_list[1])
            action_operand = int(arg_list[2])
            additional_params = arg_list[3::]#.join("")
           # additional_params = string.join(additional_params)
            print("action type:" , action_type)
            print("action time:" , action_time)
            print("action operand:" , action_operand)
            print("additional_params:" , additional_params)
            
            if(action_type == "ton"):     #ton = turn on
                led_list[action_operand].turn_on(action_time)
                
            elif(action_type == "tof"):     #tof = turn off
                led_list[action_operand].turn_off(action_time)
            
            elif(action_type == "f"):     #f = flicker
                led_list[action_operand].flicker(action_time, *additional_params)

            

    def reset_leds(self):
        for led in led_list:
            led.turn_off()
    


  








led = Light_bulb.light_bulb(2)      
led.turn_off()

                                        #reset all available leds 
led_list = [led]




if not os.path.isfile(Save_state.path):
    with open(Save_state.path , "w") as f:          #check if save file exists on first launch
        pass





# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = (IP , PORT)
sock.bind(server_address)

# Listen for incoming connections
sock.listen(5)

while True:
    # Wait for a connection
    connection, client_address = sock.accept()
    
    #start thread to handle connection
    handler_thread = handle_connection(connection , client_address)
    handler_thread.start()
    

