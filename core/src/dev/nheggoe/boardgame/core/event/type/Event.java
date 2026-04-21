package dev.nheggoe.boardgame.core.event.type;

import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.EventListener;
import dev.nheggoe.boardgame.core.event.EventPublisher;

/// Represents an abstract base for events used within the application.
///
/// This sealed interface is designed to define a common type for all event instances, enabling
/// the system to process and distinguish between different types of events. It ensures type safety
/// by restricting the permitted implementations to a predefined set of classes.
///
/// Each permitted implementation defines a specific type of event, carrying relevant information
/// that can be published, listened to, and handled by the appropriate components within the system.
///
/// This interface works in conjunction with other components such as [EventBus],
/// [EventListener], and [EventPublisher] to allow for event-driven programming paradigms.
///
/// @author Nick Heggø
/// @version 2025.05.08
public interface Event {}
